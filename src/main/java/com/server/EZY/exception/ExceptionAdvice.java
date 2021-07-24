package com.server.EZY.exception;

import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.token.exception.TokenLoggedOutException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ExceptionAdvice {
    
    String DEFAULT_EXCEPTION = "unknown";

    String CUSTOM_401_UNAUTHORIZED = "unauthorized";
    String CUSTOM_403_FORBIDDEN = "forbidden";
    String CUSTOM_404_NOT_FOUND = "not-found";

    String USER_NOT_FOUND = "member-not-found";
    String INVALID_ACCESS = "invalid-access";

    String INVALID_AUTHENTICATION_NUMBER = "invalid-authentication-number";
    String USERNAME_NOT_FOUND = "username-not-found";
    String AUTHENTICATION_NUMBER_TRANSFER_FAILED = "authentication-number-transfer-failed";

    String ACCESS_TOKEN_EXPIRED = "access-token-expired";
    String INVALID_TOKEN = "invalid-token";
    String TOKEN_LOGGED_OUT = "token-logged-out";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    CommonResult defaultException(Exception ex);

    /*** Custom Server Exception 시작***/
    @ExceptionHandler(CustomUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult unauthorized(CustomUnauthorizedException ex);

    @ExceptionHandler(CustomForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult forbiddenException(CustomForbiddenException ex);

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult notFoundException(CustomNotFoundException ex);


    /*** Member Exceptions 시작***/
    // 맴버를 찾을 수 없습니다.
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult memberNotFoundException(MemberNotFoundException ex);

    // 해당 유저이름으로 맴버를 찾을 수 없습니다.
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult usernameNotFoundException(UsernameNotFoundException ex);

    // 잘못된 접근입니다.
    @ExceptionHandler(InvalidAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult invalidAccessException(InvalidAccessException ex);


    /*** Authentication Number Exception 시작 ***/
    // 인증번호가 올바르지 않습니다
    @ExceptionHandler(InvalidAuthenticationNumberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex);

    // 인증번호 전송을 실패 했습니다.
    @ExceptionHandler(AuthenticationNumberTransferFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult authenticationNumberTransferFailedException(AuthenticationNumberTransferFailedException ex);

    /*** Token Exceptions 시작 ***/
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
}
