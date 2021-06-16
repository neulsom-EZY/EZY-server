package com.server.EZY.security;

import com.server.EZY.dto.UserDto;
import com.server.EZY.exception.AccessTokenExpiredException;
import com.server.EZY.exception.InvalidTokenException;
import com.server.EZY.exception.UserNotFoundException;
import com.server.EZY.model.user.Role;
import com.server.EZY.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MyUserDetails myUserDetails;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final UserDetails userDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessJwt = jwtTokenProvider.resolveToken(request);
        String refreshJwt = request.getHeader("RefreshToken");



        filterChain.doFilter(request, response);
    }
}
