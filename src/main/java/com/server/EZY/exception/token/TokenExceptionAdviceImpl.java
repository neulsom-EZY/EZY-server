package com.server.EZY.exception.token;

import com.server.EZY.exception.token.exception.*;
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
public class TokenExceptionAdviceImpl implements TokenExceptionAdvice{

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @Override
    public CommonResult accessTokenExpiredException(AccessTokenExpiredException ex) {
        log.debug("=== Access Token Expired Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(ACCESS_TOKEN_EXPIRED);
    }

    @Override
    public CommonResult invalidTokenException(InvalidTokenException ex) {
        log.debug("=== Invalid Token Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(INVALID_TOKEN);
    }

    @Override
    public CommonResult tokenLoggedOutException(TokenLoggedOutException ex) {
        log.debug("=== Logged Out Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(TOKEN_LOGGED_OUT);
    }

    @Override
    public CommonResult authorizationHeaderIsEmpty(AuthorizationHeaderIsEmpty ex) {
        log.debug("=== Authorization Header Is Empty Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(AUTHORIZATION_HEADER_IS_EMPTY);
    }

    @Override
    public CommonResult refreshTokenIsEmpty(RefreshTokenHeaderIsEmpty ex) {
        log.debug("=== Refresh Token Header Is Empty Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(REFRESH_TOKEN_HEADER_IS_EMPTY);
    }
}
