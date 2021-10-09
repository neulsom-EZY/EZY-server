package com.server.EZY.security.jwt;

import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.AuthorizationHeaderIsEmpty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/// 데이터베이스 호출을 수행하는 중이므로 OncePerRequestFilter을 사용하여 두 번 이상의 작업을 막는다.
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        try {
            if(token != null && !jwtTokenProvider.isTokenExpired(token)){
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (AccessTokenExpiredException e){
            // 사용자가 인증되지 않도록 보장하는 매우 중요한 코드
            SecurityContextHolder.clearContext();
            throw new AccessTokenExpiredException();
        }

        filterChain.doFilter(request, response);
    }
}
