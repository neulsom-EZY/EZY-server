package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.unknown_exception.UnknownExceptionHandler;
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
@SpringBootTest
@DisplayName("ExceptionAdvice test")
@AutoConfigureMockMvc
class UnknownExceptionHandlerTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired
    UnknownExceptionHandler unknownExceptionHandler;

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

    @Test
    @DisplayName("DefaultException 검증")
    void DefaultException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int DEFAULT_EXCEPTION_CODE_KO = getExceptionCode(UnknownExceptionHandler.DEFAULT_EXCEPTION, Locale.KOREA);
        final int USER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(UnknownExceptionHandler.DEFAULT_EXCEPTION, Locale.ENGLISH);

        final String USER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(UnknownExceptionHandler.DEFAULT_EXCEPTION, Locale.KOREA);
        final String USER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(UnknownExceptionHandler.DEFAULT_EXCEPTION, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = unknownExceptionHandler.defaultException(new Exception());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = unknownExceptionHandler.defaultException(new Exception());

        // Then
        assertEquals(DEFAULT_EXCEPTION_CODE_KO, USER_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(DEFAULT_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(USER_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }
}