package com.server.EZY.security.jwt;

import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.AuthorizationHeaderIsEmpty;
import com.server.EZY.exception.token.exception.RefreshTokenHeaderIsEmpty;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.security.authentication.MyUserDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${security.jwt.token.secretKey}")
    private String secretKey;

    /**
     * accessToken의 만료기간 (1시간 - 1hour)
     * @author 배태현
     */
    private static long TOKEN_VALIDATION_SECOND = 1000L * 60 * 60;

    /**
     * refreshToken의 만료기간 (6달 - 6month)
     * @author 배태현
     */
    public static long REFRESH_TOKEN_VALIDATION_TIME = TOKEN_VALIDATION_SECOND * 24 * 180;

    private final MyUserDetails myUserDetails;

    /**
     * secretKey를 Base64인코딩방식을 사용하여 인코딩하는 메서드
     * @author 배태현
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 사용자의 이름과 권한을 이용해서 accessToken을 만드는 메소드입니다.
     *
     * @param username 사용자의 이름
     * @param roles 사용자 권한 (ROLE_ADMIN, ROLE_CLIENT, ROLE_NOT_PERMIT)
     * @return accessToken
     * @author 배태현
     */
    public String createToken(String username, List<Role> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().
                map(s -> new SimpleGrantedAuthority(s.getAuthority())).
                filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDATION_SECOND); //Expire Time

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * refreshToken을 만드는 메소드이며 보안상의 이유로 claim에 아무정보도 들어가지 않습니다.
     * @return refreshToken
     * @author 배태현
     */
    public String createRefreshToken(){
        Claims claims = Jwts.claims().setSubject(null);

        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALIDATION_TIME); //Expire Time

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 사용자의 인증정보를 조회하는 메소드입니다.
     * @param token 사용자의 인증정보를 조회 할 token
     * @return 인증정보 (true || false)
     * @author 배태현
     */
    public Authentication getAuthentication(String token){
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * token에서 username을 추출하는 메서드입니다.
     * @param token token
     * @return username
     * @author 배태현
     */
    public String getUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    /**
     * Header에서 accessToken을 가져오는 메소드입니다.
     * @param req HttpServletRequest
     * @return accesstoken (header가 비어있다면 null)
     * @author 배태현
     */
    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    /**
     * Header에서 refreshToken을 가져오는 메소드입니다.
     * @param req HttpServletRequest
     * @return refreshToken (header가 비어있다면 null)
     * @author 배태현
     */
    public String resolveRefreshToken(HttpServletRequest req){
        String refreshToken = req.getHeader("RefreshToken");
        if(refreshToken != null && refreshToken.startsWith("Bearer ")){
            return refreshToken.substring(7);
        } else {
            return null;
        }
    }

    /**
     * JWT claim을 추출하는 메서드입니다.
     * @param token
     * @return Jwts - claims
     * @throws ExpiredJwtException JWT의 유효기간이 만료되었을 때
     * @throws IllegalArgumentException 부적절한 인자가 넘어왔을 때
     * @throws MalformedJwtException 구조적인 문제가 있는 JWT인 경우
     * @throws SignatureException JWT의 서명을 확인하지 못했을 때
     * @throws UnsupportedJwtException JWT의 형식이 원하는 형식과 맞지 않는 경우
     * @throws PrematureJwtException 접근이 허용되기 전인 JWT가 수신된 경우
     * @author 배태현
     */
    public Claims extractAllClaims(String token) throws ExpiredJwtException, IllegalArgumentException, MalformedJwtException, SignatureException, UnsupportedJwtException, PrematureJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰의 유효기간 만료를 확인하는 메서드
     * @param token
     * @return true (토큰의 유효기간이 만료되었을 경우) | false (토큰의 유효기간이 만료되지 않았을 경우)
     * @author 배태현
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 토큰을 검증하는 메서드
     * @param token
     * @return true (토큰이 제대로 검증 되었을 경우) | false (토큰에 문제가 있을 경우)
     * @author 배태현
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
