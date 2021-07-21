package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.user.UserExceptionController;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.user.exception.InvalidAuthenticationNumberException;
import com.server.EZY.exception.user.exception.UserNotFoundException;

import com.server.EZY.response.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest @DisplayName("ExceptionAdvice test")
@AutoConfigureMockMvc
class ExceptionAdviceTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired ExceptionAdvice exceptionAdvice;
    @Autowired UserExceptionController userExceptionController;

    // LocalContext의 locale을 변경한다.
    void setLocal(Locale locale){
        LocaleContextHolder.setLocale(locale);
    }
    // CommonResulte를 json 형식으로 반환한다.
    String getResult(CommonResult commonResult) throws JsonProcessingException {
        return objMapper.writeValueAsString(commonResult);
    }

    void printResult(CommonResult commonResult_KO, CommonResult commonResult_EN) throws JsonProcessingException {
        log.info("\n=== TEST result ===\nKO: {}\nEN: {}", getResult(commonResult_KO), getResult(commonResult_EN));
    }

    int getExceptionCode(String code, Locale locale){
        return Integer.parseInt(messageSource.getMessage(code + ".code", null, locale));
    }
    String getExceptionMsg(String code, Locale locale){
        return messageSource.getMessage(code + ".msg", null, locale);
    }

    @Test @DisplayName("DefaultException 검증")
    void DefaultException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int DEFAULT_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.KOREA);
        final int USER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.ENGLISH);

        final String USER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.KOREA);
        final String USER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.defaultException(new Exception());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.defaultException(new Exception());

        // Then
        assertEquals(DEFAULT_EXCEPTION_CODE_KO, USER_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(DEFAULT_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(USER_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("CustomUnauthorizedExceptio 검증")
    void CustomUnauthorizedException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.CUSTOM_401_UNAUTHORIZED, Locale.KOREA);
        final int CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.CUSTOM_401_UNAUTHORIZED, Locale.ENGLISH);
        final String CUSTOM_UNAUTHORIZED_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.CUSTOM_401_UNAUTHORIZED, Locale.KOREA);
        final String CUSTOM_UNAUTHORIZED_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.CUSTOM_401_UNAUTHORIZED, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.unauthorized(new CustomUnauthorizedException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.unauthorized(new CustomUnauthorizedException());

        // Then
        assertEquals(CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_KO, CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_EN);

        assertEquals(CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(CUSTOM_UNAUTHORIZED_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(CUSTOM_UNAUTHORIZED_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("CustomUnauthorizedExceptio 검증")
    void CustomForbiddenException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int CUSTOM_FORBIDDEN_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.CUSTOM_403_FORBIDDEN, Locale.KOREA);
        final int CUSTOM_FORBIDDEN_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.CUSTOM_403_FORBIDDEN, Locale.ENGLISH);
        final String CUSTOM_FORBIDDEN_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.CUSTOM_403_FORBIDDEN, Locale.KOREA);
        final String CUSTOM_FORBIDDEN_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.CUSTOM_403_FORBIDDEN, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.forbiddenException(new CustomForbiddenException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.forbiddenException(new CustomForbiddenException());

        // Then
        assertEquals(CUSTOM_FORBIDDEN_EXCEPTION_CODE_KO, CUSTOM_FORBIDDEN_EXCEPTION_CODE_EN);

        assertEquals(CUSTOM_FORBIDDEN_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(CUSTOM_FORBIDDEN_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(CUSTOM_FORBIDDEN_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(CUSTOM_FORBIDDEN_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("CustomUnauthorizedExceptio 검증")
    void CustomNotFoundException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int CUSTOM_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.CUSTOM_404_NOT_FOUND, Locale.KOREA);
        final int CUSTOM_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.CUSTOM_404_NOT_FOUND, Locale.ENGLISH);
        final String CUSTOM_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.CUSTOM_404_NOT_FOUND, Locale.KOREA);
        final String CUSTOM_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.CUSTOM_404_NOT_FOUND, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.notFoundException(new CustomNotFoundException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.notFoundException(new CustomNotFoundException());

        // Then
        assertEquals(CUSTOM_NOT_FOUND_EXCEPTION_CODE_KO, CUSTOM_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(CUSTOM_NOT_FOUND_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(CUSTOM_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(CUSTOM_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(CUSTOM_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("UserNotFoundException 검증")
    void UserNotFoundException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int USER_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.USER_NOT_FOUND, Locale.KOREA);
        final int USER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.USER_NOT_FOUND, Locale.ENGLISH);
        final String USER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.USER_NOT_FOUND, Locale.KOREA);
        final String USER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.USER_NOT_FOUND, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.userNotFoundException(new UserNotFoundException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.userNotFoundException(new UserNotFoundException());

        // Then
        assertEquals(USER_NOT_FOUND_EXCEPTION_CODE_KO, USER_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(USER_NOT_FOUND_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(USER_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("InvalidAccessException 검증")
    void InvalidAccessException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int INVALID_ACCESS_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.INVALID_ACCESS_EXCEPTION, Locale.KOREA);
        final int INVALID_ACCESS_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.INVALID_ACCESS_EXCEPTION, Locale.ENGLISH);
        final String INVALID_ACCESS_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.INVALID_ACCESS_EXCEPTION, Locale.KOREA);
        final String INVALID_ACCESS_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.INVALID_ACCESS_EXCEPTION, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.invalidAccessException(new InvalidAccessException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.invalidAccessException(new InvalidAccessException());

        // Then
        assertEquals(INVALID_ACCESS_EXCEPTION_CODE_KO, INVALID_ACCESS_EXCEPTION_CODE_EN);

        assertEquals(INVALID_ACCESS_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(INVALID_ACCESS_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(INVALID_ACCESS_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(INVALID_ACCESS_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("InvalidAuthenticationNumberException 검증")
    void InvalidAuthenticationNumberException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int INVALID_AUTHENTICATION_NUMBER_CODE_KO = getExceptionCode(ExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.KOREA);
        final int INVALID_AUTHENTICATION_NUMBER_CODE_EN = getExceptionCode(ExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.ENGLISH);
        final String INVALID_AUTHENTICATION_NUMBER_MSG_KO = getExceptionMsg(ExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.KOREA);
        final String INVALID_AUTHENTICATION_NUMBER_MSG_EN = getExceptionMsg(ExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.invalidAuthenticationNumberException(new InvalidAuthenticationNumberException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.invalidAuthenticationNumberException(new InvalidAuthenticationNumberException());

        // Then
        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_KO, INVALID_AUTHENTICATION_NUMBER_CODE_EN);

        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_KO, commonResult_KO.getCode());
        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_EN, commonResult_EN.getCode());

        assertEquals(INVALID_AUTHENTICATION_NUMBER_MSG_KO, commonResult_KO.getMassage());
        assertEquals(INVALID_AUTHENTICATION_NUMBER_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }


    @Test @DisplayName("AccessTokenException 검증")
    void AccessTokenException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int ACCESS_TOKEN_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.ACCESS_TOKEN_EXPIRED, Locale.KOREA);
        final int ACCESS_TOKEN_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.ACCESS_TOKEN_EXPIRED, Locale.ENGLISH);
        final String ACCESS_TOKEN_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.ACCESS_TOKEN_EXPIRED, Locale.KOREA);
        final String ACCESS_TOKEN_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.ACCESS_TOKEN_EXPIRED, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.accessTokenExpiredException(new AccessTokenExpiredException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.accessTokenExpiredException(new AccessTokenExpiredException());

        // Then
        assertEquals(ACCESS_TOKEN_EXCEPTION_CODE_KO, ACCESS_TOKEN_EXCEPTION_CODE_EN);

        assertEquals(ACCESS_TOKEN_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(ACCESS_TOKEN_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(ACCESS_TOKEN_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(ACCESS_TOKEN_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("InvalidException 검증")
    void InvalidException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int INVALID_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.INVALID_TOKEN, Locale.KOREA);
        final int INVALID_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.INVALID_TOKEN, Locale.ENGLISH);
        final String INVALID_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.INVALID_TOKEN, Locale.KOREA);
        final String INVALID_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.INVALID_TOKEN, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.invalidTokenException(new InvalidTokenException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.invalidTokenException(new InvalidTokenException());

        // Then
        assertEquals(INVALID_EXCEPTION_CODE_KO, INVALID_EXCEPTION_CODE_EN);

        assertEquals(INVALID_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(INVALID_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(INVALID_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(INVALID_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }
}