package com.server.EZY.security.exception_hendler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.token.TokenExceptionHandler;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.unknown_exception.UnknownExceptionHandler;
import com.server.EZY.response.result.CommonResult;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try{
            filterChain.doFilter(request, response);
        } catch(ExpiredJwtException e) {
            log.debug("=== Filter에서 ExpiredJwtException이 발생했습니다.");
            tokenExceptionHandler.expiredJwtException(e);
        } catch(JwtException | IllegalArgumentException e) {
            log.debug("=== Filter에서 올바르지 않는 Jwt Exception이 발생했습니다. ===");
            tokenExceptionHandler.invalidTokenException(new InvalidTokenException());
        } catch(Exception e){
            log.error("=== Filter에서 알 수 없는 Exception이 발생했습니다. ===");
            setResponseForExceptionResult(response, HttpStatus.INTERNAL_SERVER_ERROR, unknownExceptionHandler.defaultException(e));
        }
    }

    /**
     * Exception결과에 대한 정보를 Http Status Code, json response객체에 씁니다.
     * @param response response
     * @param httpStatus 사용자에게 반환할 HttpStatusCode
     * @param commonResult 사용자에게 반환할 Exception결과를 가지고 있는 객체
     * @throws IOException HttpServletResponse에 writer를 통해 body를 작성하면 발생할 수 있지만 그럴 가능성은 거의 없다.
     */
    private void setResponseForExceptionResult(HttpServletResponse response, HttpStatus httpStatus, CommonResult commonResult) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(commonResultToJson(commonResult));
    }

    /**
     * CommonResult타입의 객체를 json으로 변환한다.
     * @param commonResult 사용자에게 반환할 정보를 가지고 있는 commonResult
     * @return 해당 commonResult를 json으로 변환한 값
     * @throws JsonProcessingException - Json으로 변환시 발생할 수 있지만 발생할 가능성이 거의 없다.
     */
    private String commonResultToJson(CommonResult commonResult) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(commonResult);
    }

}
