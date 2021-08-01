package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.authenticationNumber.AuthenticationNumberExceptionAdvice;
import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
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
public class AuthenticationNumberExceptionAdviceTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired AuthenticationNumberExceptionAdvice authenticationNumberExceptionAdvice;

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
        final int INVALID_AUTHENTICATION_NUMBER_CODE_KO = getExceptionCode(AuthenticationNumberExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.KOREA);
        final int INVALID_AUTHENTICATION_NUMBER_CODE_EN = getExceptionCode(AuthenticationNumberExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.ENGLISH);
        final String INVALID_AUTHENTICATION_NUMBER_MSG_KO = getExceptionMsg(AuthenticationNumberExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.KOREA);
        final String INVALID_AUTHENTICATION_NUMBER_MSG_EN = getExceptionMsg(AuthenticationNumberExceptionAdvice.INVALID_AUTHENTICATION_NUMBER, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = authenticationNumberExceptionAdvice.invalidAuthenticationNumberException(new InvalidAuthenticationNumberException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = authenticationNumberExceptionAdvice.invalidAuthenticationNumberException(new InvalidAuthenticationNumberException());

        // Then
        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_KO, INVALID_AUTHENTICATION_NUMBER_CODE_EN);

        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_KO, commonResult_KO.getCode());
        assertEquals(INVALID_AUTHENTICATION_NUMBER_CODE_EN, commonResult_EN.getCode());

        assertEquals(INVALID_AUTHENTICATION_NUMBER_MSG_KO, commonResult_KO.getMassage());
        assertEquals(INVALID_AUTHENTICATION_NUMBER_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test
    @DisplayName("AuthenticationNumberTransferFailedException 검증")
    void AuthenticationNumberTransferFailedException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_KO = getExceptionCode(AuthenticationNumberExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.KOREA);
        final int AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_EN = getExceptionCode(AuthenticationNumberExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.ENGLISH);
        final String AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_KO = getExceptionMsg(AuthenticationNumberExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.KOREA);
        final String AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_EN = getExceptionMsg(AuthenticationNumberExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = authenticationNumberExceptionAdvice.authenticationNumberTransferFailedException(new AuthenticationNumberTransferFailedException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = authenticationNumberExceptionAdvice.authenticationNumberTransferFailedException(new AuthenticationNumberTransferFailedException());

        // Then
        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_KO, AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_EN);

        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_KO, commonResult_KO.getCode());
        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_EN, commonResult_EN.getCode());

        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_KO, commonResult_KO.getMassage());
        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }
}
