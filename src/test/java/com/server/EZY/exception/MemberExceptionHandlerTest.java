package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.user.MemberExceptionHandler;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.user.exception.MemberAlreadyExistException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.response.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@DisplayName("ExceptionAdvice test")
@AutoConfigureMockMvc
public class MemberExceptionHandlerTest {

    @Autowired
    ObjectMapper objMapper;
    @Autowired
    MessageSource messageSource;
    @Autowired
    MemberExceptionHandler memberExceptionHandler;

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
    @DisplayName("memberNotFoundException 검증")
    void MemberNotFoundException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(MemberExceptionHandler.MEMBER_NOT_FOUND, Locale.KOREA);
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(MemberExceptionHandler.MEMBER_NOT_FOUND, Locale.ENGLISH);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(MemberExceptionHandler.MEMBER_NOT_FOUND, Locale.KOREA);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(MemberExceptionHandler.MEMBER_NOT_FOUND, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = memberExceptionHandler.memberNotFoundException(new MemberNotFoundException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = memberExceptionHandler.memberNotFoundException(new MemberNotFoundException());

        // Then
        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_CODE_KO, MEMBER_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("memberAlreadyExistException 검증")
    void MemberAlreadyExistException() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(MemberExceptionHandler.MEMBER_ALREADY_EXIST, Locale.KOREA);
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(MemberExceptionHandler.MEMBER_ALREADY_EXIST, Locale.ENGLISH);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(MemberExceptionHandler.MEMBER_ALREADY_EXIST, Locale.KOREA);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(MemberExceptionHandler.MEMBER_ALREADY_EXIST, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = memberExceptionHandler.memberAlreadyExistException(new MemberAlreadyExistException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = memberExceptionHandler.memberAlreadyExistException(new MemberAlreadyExistException());

        // Then
        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_CODE_KO, MEMBER_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(MEMBER_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("UsernameNotFoundException 검증")
    void UsernameNotFoundException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int USERNAME_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(MemberExceptionHandler.USERNAME_NOT_FOUND, Locale.KOREA);
        final int USERNAME_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(MemberExceptionHandler.USERNAME_NOT_FOUND, Locale.ENGLISH);
        final String USERNAME_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(MemberExceptionHandler.USERNAME_NOT_FOUND, Locale.KOREA);
        final String USERNAME_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(MemberExceptionHandler.USERNAME_NOT_FOUND, Locale.ENGLISH);

        final String username = "siwony_";
        // When
        CommonResult commonResult_KO = memberExceptionHandler.usernameNotFoundException(new UsernameNotFoundException(username));
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = memberExceptionHandler.usernameNotFoundException(new UsernameNotFoundException(username));

        // Then
        assertEquals(USERNAME_NOT_FOUND_EXCEPTION_CODE_KO, USERNAME_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(USERNAME_NOT_FOUND_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(USERNAME_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(USERNAME_NOT_FOUND_EXCEPTION_MSG_KO.replaceAll(":username","'" + username + "'"), commonResult_KO.getMassage());
        assertEquals(USERNAME_NOT_FOUND_EXCEPTION_MSG_EN.replaceAll(":username","'" + username + "'"), commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("InvalidAccessException 검증")
    void InvalidAccessException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int INVALID_ACCESS_EXCEPTION_CODE_KO = getExceptionCode(MemberExceptionHandler.INVALID_ACCESS, Locale.KOREA);
        final int INVALID_ACCESS_EXCEPTION_CODE_EN = getExceptionCode(MemberExceptionHandler.INVALID_ACCESS, Locale.ENGLISH);
        final String INVALID_ACCESS_EXCEPTION_MSG_KO = getExceptionMsg(MemberExceptionHandler.INVALID_ACCESS, Locale.KOREA);
        final String INVALID_ACCESS_EXCEPTION_MSG_EN = getExceptionMsg(MemberExceptionHandler.INVALID_ACCESS, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = memberExceptionHandler.invalidAccessException(new InvalidAccessException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = memberExceptionHandler.invalidAccessException(new InvalidAccessException());

        // Then
        assertEquals(INVALID_ACCESS_EXCEPTION_CODE_KO, INVALID_ACCESS_EXCEPTION_CODE_EN);

        assertEquals(INVALID_ACCESS_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(INVALID_ACCESS_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(INVALID_ACCESS_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(INVALID_ACCESS_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }
}
