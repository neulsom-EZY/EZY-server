package com.server.EZY.exception.authentication_number;

import com.server.EZY.exception.authentication_number.exception.FailedToSendMessageException;
import com.server.EZY.exception.authentication_number.exception.InvalidAuthenticationNumberException;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice @Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class AuthenticationNumberExceptionHandlerImpl implements AuthenticationNumberExceptionHandler {

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @Override
    public CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex) {
        log.debug("=== InvalidAuthenticationNumberException 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(INVALID_AUTHENTICATION_NUMBER);
    }

    @Override
    public CommonResult failedToSendMessageException(FailedToSendMessageException ex) {
        log.debug("=== FailedToSendMessageException 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(FAIL_TO_SEND_MESSAGE);
    }
}
