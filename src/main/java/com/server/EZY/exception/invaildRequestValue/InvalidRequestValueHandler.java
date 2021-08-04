package com.server.EZY.exception.invaildRequestValue;

import com.server.EZY.response.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice @Order(Ordered.HIGHEST_PRECEDENCE)
public class InvalidRequestValueHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private CommonResult invalidRequestValueHandler(MethodArgumentNotValidException ex){
        String getExceptionParameter = ex.getParameter().getParameterName();
        log.debug("getExceptionParameter : {}", getExceptionParameter);
        return null;
    }
}
