package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.user.UserExceptionController;
import com.server.EZY.exception.user.exception.UserNotFoundException;

import com.server.EZY.response.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        log.info("\n=== TEST result ===\n{}\n{}", getResult(commonResult_KO), getResult(commonResult_EN));
    }

    int getExceptionCode(String code, Locale locale){
        return Integer.parseInt(messageSource.getMessage(code + ".code", null, locale));
    }
    String getExceptionMsg(String code, Locale locale){
        return messageSource.getMessage(code + ".msg", null, locale);
    }

    @Test @DisplayName("UserNotFoundException 검증")
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


}