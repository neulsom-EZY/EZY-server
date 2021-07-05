package com.server.EZY.exception;

import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.user.exception.UserNotFoundException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    CommonResult defaultException(Exception ex);

    //*** User Exceptions ***//
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult userNotFoundException(Exception ex);

    //*** Token Exceptions ***//
    @ExceptionHandler(AccessTokenExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult accessTokenExpiredException(Exception ex);

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult invalidTokenException(InvalidTokenException ex);
}
