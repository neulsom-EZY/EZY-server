package com.server.EZY.exception.customError;

import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
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
public class CustomErrorAdviceImpl implements CustomErrorAdvice {

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @Override
    public CommonResult unauthorized(CustomUnauthorizedException ex) {
        log.debug("=== 401 Unauthorized Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_401_UNAUTHORIZED);
    }

    @Override
    public CommonResult forbiddenException(CustomForbiddenException ex) {
        log.debug("=== 403 Forbidden Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_403_FORBIDDEN);
    }

    @Override
    public CommonResult notFoundException(CustomNotFoundException ex) {
        log.debug("=== 404 NotFound Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_404_NOT_FOUND);
    }
}
