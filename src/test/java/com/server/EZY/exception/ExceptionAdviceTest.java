package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.exception.token.exception.InvalidTokenException;
import com.server.EZY.exception.user.UserExceptionController;
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

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest @DisplayName("ExceptionAdvice test")
@AutoConfigureMockMvc
class ExceptionAdviceTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired ExceptionAdvice exceptionAdvice;
    @Autowired UserExceptionController userExceptionController;

    String objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
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