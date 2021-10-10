package com.server.EZY.security.exception_hendler;

import com.server.EZY.exception.token.TokenExceptionHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter에서 발생한 Exception을 Handling하기 위한 필터
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class FilterExceptionHandlerFilter extends OncePerRequestFilter {

    private final TokenExceptionHandlerImpl tokenExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
        //TODO JwtTokenFilter를 참고하여 Exception 처리
    }
}
