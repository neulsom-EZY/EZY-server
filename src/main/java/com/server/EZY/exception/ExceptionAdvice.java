package com.server.EZY.exception;

import com.server.EZY.exception.custom.exception.CustomForbiddenException;
import com.server.EZY.exception.custom.exception.CustomNotFoundException;
import com.server.EZY.exception.custom.exception.CustomUnauthorizedException;
import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.user.exception.UserNotFoundException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ExceptionAdvice {
    
    String DEFAULT_EXCEPTION = "unknown";

    String CUSTOM_401_UNAUTHORIZED = "unauthorized";
    String CUSTOM_403_FORBIDDEN = "forbidden";
    String CUSTOM_404_NOT_FOUND = "not-found";

    String USER_NOT_FOUND = "user-not-found";

    String ACCESS_TOKEN_EXPIRED = "access-token-expired";
    String INVALID_TOKEN = "invalid-token";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    CommonResult defaultException(Exception ex);

    /*** Custom Server Exception ***/

    @ExceptionHandler(CustomUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult unauthorized(CustomUnauthorizedException ex);

    @ExceptionHandler(CustomForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult forbiddenException(CustomForbiddenException ex);

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult notFoundException(CustomNotFoundException ex);
    /*** User Exceptions ***/

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult userNotFoundException(UserNotFoundException ex);

    /*** Token Exceptions ***/
    // 액세스 토큰이 만료되었습니다.
    @ExceptionHandler(AccessTokenExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult accessTokenExpiredException(AccessTokenExpiredException ex);

    // 올바르지 않는 토큰
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult invalidTokenException(InvalidTokenException ex);
}
