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

    private long TOKEN_VALIDATION_SECOND = 360000; // 1h  360000
    private long REFRESH_TOKEN_VALIDATION_TIME = TOKEN_VALIDATION_SECOND * 24 * 180; //6months

    private final MyUserDetails myUserDetails;

    // Base64 encoded secret key
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //token 생성
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

    // token에서 인증정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //token에서 회원 정보 추출
    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // header(Authorization)에서 token 값 가져오기
    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return  bearerToken.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest req){
        String refreshToken = req.getHeader("RefreshToken");
        if(refreshToken != null){
            return  refreshToken.substring(7);
        }
        return null;
    }

    //token 검증 (유효성, 만료일자 확인)
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
