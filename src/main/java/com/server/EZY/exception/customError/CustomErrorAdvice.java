package com.server.EZY.exception.customError;

import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface CustomErrorAdvice {

    String CUSTOM_401_UNAUTHORIZED = "unauthorized";
    String CUSTOM_403_FORBIDDEN = "forbidden";
    String CUSTOM_404_NOT_FOUND = "not-found";

    @ExceptionHandler(CustomUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult unauthorized(CustomUnauthorizedException ex);

    @ExceptionHandler(CustomForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult forbiddenException(CustomForbiddenException ex);

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult notFoundException(CustomNotFoundException ex);
}
