package com.server.EZY.exception.unknown_exception;

import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice @Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class UnknownExceptionHandler {

    public static String DEFAULT_EXCEPTION = "unknown";

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    // 알수없는 에러
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult defaultException(Exception ex){
        log.error("=== 알 수 없는 애러 발생 ===", ex);
        return exceptionResponseObjectUtil.getExceptionResponseObj(DEFAULT_EXCEPTION);
    }
}
