package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.authentication_number.AuthenticationNumberExceptionHandler;
import com.server.EZY.exception.authentication_number.exception.FailedToSendMessageException;
import com.server.EZY.exception.authentication_number.exception.InvalidAuthenticationNumberException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@DisplayName("AuthenticationNumberExceptionAdvice test")
@AutoConfigureMockMvc
public class AuthenticationNumberExceptionHandlerTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired
    AuthenticationNumberExceptionHandler authenticationNumberExceptionHandler;

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
    @DisplayName("InvalidAuthenticationNumberException 검증")
    void InvalidAuthenticationNumberException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int INVALID_AUTHENTICATION_NUMBER_CODE_KO = getExceptionCode(AuthenticationNumberExceptionHandler.INVALID_AUTHENTICATION_NUMBER, Locale.KOREA);
        final int INVALID_AUTHENTICATION_NUMBER_CODE_EN = getExceptionCode(AuthenticationNumberExceptionHandler.INVALID_AUTHENTICATION_NUMBER, Locale.ENGLISH);
        final String INVALID_AUTHENTICATION_NUMBER_MSG_KO = getExceptionMsg(AuthenticationNumberExceptionHandler.INVALID_AUTHENTICATION_NUMBER, Locale.KOREA);
        final String INVALID_AUTHENTICATION_NUMBER_MSG_EN = getExceptionMsg(AuthenticationNumberExceptionHandler.INVALID_AUTHENTICATION_NUMBER, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = authenticationNumberExceptionHandler.invalidAuthenticationNumberException(new InvalidAuthenticationNumberException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = authenticationNumberExceptionHandler.invalidAuthenticationNumberException(new InvalidAuthenticationNumberException());

        // Then
        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_KO, INVALID_AUTHENTICATION_NUMBER_CODE_EN);

        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_KO, commonResult_KO.getCode());
        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_EN, commonResult_EN.getCode());

        assertEquals(INVALID_AUTHENTICATION_NUMBER_MSG_KO, commonResult_KO.getMassage());
        assertEquals(INVALID_AUTHENTICATION_NUMBER_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test
    @DisplayName("FailToSendMessageException 검증")
    void FailToSendMessageException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int FAIL_TO_SEND_MESSAGE_CODE_KO = getExceptionCode(AuthenticationNumberExceptionHandler.FAIL_TO_SEND_MESSAGE, Locale.KOREA);
        final int FAIL_TO_SEND_MESSAGE_CODE_EN = getExceptionCode(AuthenticationNumberExceptionHandler.FAIL_TO_SEND_MESSAGE, Locale.ENGLISH);
        final String FAIL_TO_SEND_MESSAGE_MSG_KO = getExceptionMsg(AuthenticationNumberExceptionHandler.FAIL_TO_SEND_MESSAGE, Locale.KOREA);
        final String FAIL_TO_SEND_MESSAGE_MSG_EN = getExceptionMsg(AuthenticationNumberExceptionHandler.FAIL_TO_SEND_MESSAGE, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = authenticationNumberExceptionHandler.failedToSendMessageException(new FailedToSendMessageException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = authenticationNumberExceptionHandler.failedToSendMessageException(new FailedToSendMessageException());

        // Then
        assertEquals(FAIL_TO_SEND_MESSAGE_CODE_KO, FAIL_TO_SEND_MESSAGE_CODE_EN);

        assertEquals(FAIL_TO_SEND_MESSAGE_CODE_KO, commonResult_KO.getCode());
        assertEquals(FAIL_TO_SEND_MESSAGE_CODE_EN, commonResult_EN.getCode());

        assertEquals(FAIL_TO_SEND_MESSAGE_MSG_KO, commonResult_KO.getMassage());
        assertEquals(FAIL_TO_SEND_MESSAGE_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }
}
