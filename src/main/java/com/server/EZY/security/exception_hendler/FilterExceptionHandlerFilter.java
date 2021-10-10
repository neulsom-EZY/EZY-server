package com.server.EZY.security.exception_hendler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.token.TokenExceptionHandler;
import com.server.EZY.exception.token.TokenExceptionHandlerImpl;
import com.server.EZY.exception.unknown_exception.UnknownExceptionHandler;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter에서 발생한 Exception을 Handling하기 위한 Filter
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FilterExceptionHandlerFilter extends OncePerRequestFilter {

    private final TokenExceptionHandler tokenExceptionHandler;
    private final UnknownExceptionHandler unknownExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch(Exception e){
            log.debug("=== Filter에서 알 수 없는 Exception이 발생했습니다. ===\n원인: {}", e.getMessage());
            unknownExceptionHandler.defaultException(e);
        }
    }

    /**
     * CommonResult타입의 객체를 json으로 변환
     * @param commonResult 사용제에게 반환할 정보를 가지고 있는 commonResult
     * @return 해당 commonResult를 json으로 변환한 값
     * @throws JsonProcessingException - Json으로 변환시 발생할 수 있지만 발생할 일이 없음
     */
    private String commonResultToJson(CommonResult commonResult) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(commonResult);
    }


}
