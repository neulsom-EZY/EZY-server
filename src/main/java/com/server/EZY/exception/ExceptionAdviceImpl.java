package com.server.EZY.exception;

import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.user.exception.InvalidAuthenticationNumberException;
import com.server.EZY.exception.user.exception.UserNotFoundException;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdviceImpl implements ExceptionAdvice{

    private final ResponseService responseService;
    private final MessageSource messageSource;

    // code 정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code){
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * Exception 처리시 i18n애 정의 되어있는 exception code, msg를 가져온다.
     * @param code exception 식별 이름
     * @return CommonResult - 실패 객체
     */
    private CommonResult getExceptionResponseObj(String code){
        return responseService.getFailResult(Integer.parseInt(getMessage(code + ".code")), getMessage(code + ".msg"));
    }

    // 알수없는 에러
    @Override
    public CommonResult defaultException(Exception ex){
        log.error("=== 알 수 없는 애러 발생 ===", ex);
        return getExceptionResponseObj(DEFAULT_EXCEPTION);
    }


    /*** Custom Server Exception 시작 ***/
    @Override
    public CommonResult unauthorized(CustomUnauthorizedException ex) {
        log.debug("=== 401 Unauthorized Exception 발생 ===");
        return getExceptionResponseObj(CUSTOM_401_UNAUTHORIZED);
    }

    @Override
    public CommonResult forbiddenException(CustomForbiddenException ex) {
        log.debug("=== 403 Forbidden Exception 발생 ===");
        return getExceptionResponseObj(CUSTOM_403_FORBIDDEN);
    }

    @Override
    public CommonResult notFoundException(CustomNotFoundException ex) {
        log.debug("=== 404 NotFound Exception 발생 ===");
        return getExceptionResponseObj(CUSTOM_404_NOT_FOUND);
    }

    /*** UserException 시작 ***/
    @Override
    public CommonResult userNotFoundException(UserNotFoundException ex){
        log.debug("=== User Not Found Exception 발생 ===");
        return getExceptionResponseObj(USER_NOT_FOUND);
    }

    @Override
    public CommonResult invalidAccessException(InvalidAccessException ex) {
        log.debug("=== InvalidAccessException 발생 ===");
        return getExceptionResponseObj(INVALID_ACCESS_EXCEPTION);
    }

    @Override
    public CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex) {
        log.debug("=== InvalidAuthenticationNumberException 발생 ===");
        return getExceptionResponseObj(INVALID_AUTHENTICATION_NUMBER);
    }

    /*** Token Exceptions 시작 ***/
    @Override
    public CommonResult accessTokenExpiredException(AccessTokenExpiredException ex) {
        log.debug("=== Access Token Expired Exception 발생 ===");
        return getExceptionResponseObj(ACCESS_TOKEN_EXPIRED);
    }

    @Override
    public CommonResult invalidTokenException(InvalidTokenException ex) {
        log.debug("=== Invalid Token Exception 발생 ===");
        return getExceptionResponseObj(INVALID_TOKEN);
    }


}
