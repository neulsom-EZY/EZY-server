package com.server.EZY.exception.authenticationNumber;

import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AuthenticationNumberExceptionAdvice {

    String INVALID_AUTHENTICATION_NUMBER = "invalid-authentication-number";
    String AUTHENTICATION_NUMBER_TRANSFER_FAILED = "authentication-number-transfer-failed";

    // 인증번호가 올바르지 않습니다
    @ExceptionHandler(InvalidAuthenticationNumberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex);

    // 인증번호 전송을 실패 했습니다.
    @ExceptionHandler(AuthenticationNumberTransferFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult authenticationNumberTransferFailedException(AuthenticationNumberTransferFailedException ex);
}
