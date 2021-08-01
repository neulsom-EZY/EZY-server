package com.server.EZY.exception;

import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.exception.token.exception.*;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
import com.server.EZY.exception.user.exception.MemberAlreadyExistException;
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

    String INVALID_AUTHENTICATION_NUMBER = "invalid-authentication-number";
    String AUTHENTICATION_NUMBER_TRANSFER_FAILED = "authentication-number-transfer-failed";

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    CommonResult defaultException(Exception ex);

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

    /*** Authentication Number Exception 시작 ***/
    // 인증번호가 올바르지 않습니다
    @ExceptionHandler(InvalidAuthenticationNumberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex);

    // 인증번호 전송을 실패 했습니다.
    @ExceptionHandler(AuthenticationNumberTransferFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult authenticationNumberTransferFailedException(AuthenticationNumberTransferFailedException ex);
}
