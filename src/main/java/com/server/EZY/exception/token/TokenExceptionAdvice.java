package com.server.EZY.exception.token;

import com.server.EZY.exception.token.exception.*;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface TokenExceptionAdvice {

    String ACCESS_TOKEN_EXPIRED = "access-token-expired";
    String INVALID_TOKEN = "invalid-token";
    String TOKEN_LOGGED_OUT = "token-logged-out";
    String AUTHORIZATION_HEADER_IS_EMPTY = "authorization-header-is-empty";
    String REFRESH_TOKEN_HEADER_IS_EMPTY = "refresh-token-header-is-empty";

    // 액세스 토큰이 만료되었습니다.
    @ExceptionHandler(AccessTokenExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult accessTokenExpiredException(AccessTokenExpiredException ex);

    // 올바르지 않는 토큰
    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult invalidTokenException(InvalidTokenException ex);

    // 로그아웃된 토큰
    @ExceptionHandler(TokenLoggedOutException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult tokenLoggedOutException(TokenLoggedOutException ex);

    // Authorization 헤더가 비었습니다.
    @ExceptionHandler(AuthorizationHeaderIsEmpty.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    CommonResult authorizationHeaderIsEmpty(AuthorizationHeaderIsEmpty ex);

    // RefreshToken 헤더가 비었습니다.
    @ExceptionHandler(RefreshTokenHeaderIsEmpty.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    CommonResult refreshTokenIsEmpty(RefreshTokenHeaderIsEmpty ex);
}
