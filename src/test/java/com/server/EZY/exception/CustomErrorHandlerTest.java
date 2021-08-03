package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.customError.CustomErrorHandler;
import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;

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
class CustomErrorHandlerTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired
    CustomErrorHandler exceptionAdvice;

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

    @Test @DisplayName("CustomUnauthorizedExceptio 검증")
    void CustomUnauthorizedException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_KO = getExceptionCode(CustomErrorHandler.CUSTOM_401_UNAUTHORIZED, Locale.KOREA);
        final int CUSTOM_UNAUTHORIZED_EXCEPTION_CODE_EN = getExceptionCode(CustomErrorHandler.CUSTOM_401_UNAUTHORIZED, Locale.ENGLISH);
        final String CUSTOM_UNAUTHORIZED_EXCEPTION_MSG_KO = getExceptionMsg(CustomErrorHandler.CUSTOM_401_UNAUTHORIZED, Locale.KOREA);
        final String CUSTOM_UNAUTHORIZED_EXCEPTION_MSG_EN = getExceptionMsg(CustomErrorHandler.CUSTOM_401_UNAUTHORIZED, Locale.ENGLISH);

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
        final int CUSTOM_FORBIDDEN_EXCEPTION_CODE_KO = getExceptionCode(CustomErrorHandler.CUSTOM_403_FORBIDDEN, Locale.KOREA);
        final int CUSTOM_FORBIDDEN_EXCEPTION_CODE_EN = getExceptionCode(CustomErrorHandler.CUSTOM_403_FORBIDDEN, Locale.ENGLISH);
        final String CUSTOM_FORBIDDEN_EXCEPTION_MSG_KO = getExceptionMsg(CustomErrorHandler.CUSTOM_403_FORBIDDEN, Locale.KOREA);
        final String CUSTOM_FORBIDDEN_EXCEPTION_MSG_EN = getExceptionMsg(CustomErrorHandler.CUSTOM_403_FORBIDDEN, Locale.ENGLISH);

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
        final int CUSTOM_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(CustomErrorHandler.CUSTOM_404_NOT_FOUND, Locale.KOREA);
        final int CUSTOM_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(CustomErrorHandler.CUSTOM_404_NOT_FOUND, Locale.ENGLISH);
        final String CUSTOM_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(CustomErrorHandler.CUSTOM_404_NOT_FOUND, Locale.KOREA);
        final String CUSTOM_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(CustomErrorHandler.CUSTOM_404_NOT_FOUND, Locale.ENGLISH);

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
}