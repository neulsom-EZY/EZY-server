package com.server.EZY.exception.authenticationNumber;

import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AuthenticationNumberExceptionAdviceImpl implements AuthenticationNumberExceptionAdvice{

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @Override
    public CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex) {
        log.debug("=== InvalidAuthenticationNumberException 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(INVALID_AUTHENTICATION_NUMBER);
    }

    @Override
    public CommonResult authenticationNumberTransferFailedException(AuthenticationNumberTransferFailedException ex) {
        log.debug("=== AuthenticationNumberTransferFailedException 발생");
        return exceptionResponseObjectUtil.getExceptionResponseObj(AUTHENTICATION_NUMBER_TRANSFER_FAILED);
    }
}
