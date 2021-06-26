package com.server.EZY.security.jwt;

import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.security.Authentication.MyUserDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${security.jwt.token.secretKey}")
    private String secretKey;

    /**
     * accessToken의 만료기간 (1시간 - 1hour)
     * @author 배태현
     */
    private long TOKEN_VALIDATION_SECOND = 360000;

    /**
     * refreshToken의 만료기간 (6달 - 6month)
     * @author 배태현
     */
    private long REFRESH_TOKEN_VALIDATION_TIME = TOKEN_VALIDATION_SECOND * 24 * 180;

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
     * token을 복호화 하여 claim에서 사용자 이름(Username)을 가져오는 메소드입니다.
     * @param token Username을 추출 할 token
     * @return username
     * @author 배태현
     */
    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * http header에서 accessToken을 가져오는 메소드입니다.
     * @param req HttpServletRequest
     * @return true = accesstoken, false = null / (accessToken == null) - 다시 로그인 해주세요
     * @author 배태현
     */
    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken == null) {
            return "다시 로그인 해주세요";
        }
        if(bearerToken.startsWith("Bearer ")){
            return  bearerToken.substring(7);
        } else {
            return null;
        }
    }

    /**
     * http header에서 refreshToken을 가져오는 메소드입니다.
     * @param req HttpServletRequest
     * @return true = refreshToken, false = null
     * @author 배태현
     */
    public String resolveRefreshToken(HttpServletRequest req){
        String refreshToken = req.getHeader("RefreshToken");
        if(refreshToken != null){
            return  refreshToken.substring(7);
        } else {
            return null;
        }
    }

    /**
     * token을 검증하는 메소드 (유효성, 만료일자 검증)
     * @param token 검증 할 token
     * @return 토큰이 검증됨 = true, 토큰이 검증되지않음 = false
     * @author 배태현
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date()); //유효기간 만료 시 false 반환
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            return false;
        }
    }
}
