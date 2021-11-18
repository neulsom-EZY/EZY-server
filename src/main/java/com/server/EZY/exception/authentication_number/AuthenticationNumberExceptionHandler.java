package com.server.EZY.exception.authentication_number;

import com.server.EZY.exception.authentication_number.exception.FailedToSendMessageException;
import com.server.EZY.exception.authentication_number.exception.InvalidAuthenticationNumberException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AuthenticationNumberExceptionHandler {

    String INVALID_AUTHENTICATION_NUMBER = "invalid-authentication-number";
    String FAIL_TO_SEND_MESSAGE = "fail-to-send-message";

    // 인증번호가 올바르지 않습니다
    @ExceptionHandler(InvalidAuthenticationNumberException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex);

    // 문자 전송을 실패 했습니다.
    @ExceptionHandler(FailedToSendMessageException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult failedToSendMessageException(FailedToSendMessageException ex);
}
