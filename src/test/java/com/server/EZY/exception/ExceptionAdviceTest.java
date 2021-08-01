package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.exception.token.exception.*;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
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

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest @DisplayName("ExceptionAdvice test")
@AutoConfigureMockMvc
class ExceptionAdviceTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired ExceptionAdvice exceptionAdvice;

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

//    @Test @DisplayName("DefaultException 검증")
//    void DefaultException_검증() throws Exception {
//        // Given
//        setLocal(Locale.KOREA);
//        final int DEFAULT_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.KOREA);
//        final int USER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.ENGLISH);
//
//        final String USER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.KOREA);
//        final String USER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.DEFAULT_EXCEPTION, Locale.ENGLISH);
//
//        // When
//        CommonResult commonResult_KO = exceptionAdvice.defaultException(new Exception());
//        setLocal(Locale.ENGLISH);
//        CommonResult commonResult_EN = exceptionAdvice.defaultException(new Exception());
//
//        // Then
//        assertEquals(DEFAULT_EXCEPTION_CODE_KO, USER_NOT_FOUND_EXCEPTION_CODE_EN);
//
//        assertEquals(DEFAULT_EXCEPTION_CODE_KO, commonResult_KO.getCode());
//        assertEquals(USER_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());
//
//        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
//        assertEquals(USER_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());
//
//        printResult(commonResult_KO, commonResult_EN);
//    }

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

    @Test @DisplayName("memberNotFoundException 검증")
    void MemberNotFoundException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.MEMBER_NOT_FOUND, Locale.KOREA);
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.MEMBER_NOT_FOUND, Locale.ENGLISH);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.MEMBER_NOT_FOUND, Locale.KOREA);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.MEMBER_NOT_FOUND, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.memberNotFoundException(new MemberNotFoundException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.memberNotFoundException(new MemberNotFoundException());

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
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.MEMBER_ALREADY_EXIST, Locale.KOREA);
        final int MEMBER_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.MEMBER_ALREADY_EXIST, Locale.ENGLISH);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.MEMBER_ALREADY_EXIST, Locale.KOREA);
        final String MEMBER_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.MEMBER_ALREADY_EXIST, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.memberAlreadyExistException(new MemberAlreadyExistException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.memberAlreadyExistException(new MemberAlreadyExistException());

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
        final int USERNAME_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.USERNAME_NOT_FOUND, Locale.KOREA);
        final int USERNAME_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.USERNAME_NOT_FOUND, Locale.ENGLISH);
        final String USERNAME_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.USERNAME_NOT_FOUND, Locale.KOREA);
        final String USERNAME_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.USERNAME_NOT_FOUND, Locale.ENGLISH);

        final String username = "siwony_";
        // When
        CommonResult commonResult_KO = exceptionAdvice.usernameNotFoundException(new UsernameNotFoundException(username));
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.usernameNotFoundException(new UsernameNotFoundException(username));

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
        final int INVALID_ACCESS_EXCEPTION_CODE_KO = getExceptionCode(ExceptionAdvice.INVALID_ACCESS, Locale.KOREA);
        final int INVALID_ACCESS_EXCEPTION_CODE_EN = getExceptionCode(ExceptionAdvice.INVALID_ACCESS, Locale.ENGLISH);
        final String INVALID_ACCESS_EXCEPTION_MSG_KO = getExceptionMsg(ExceptionAdvice.INVALID_ACCESS, Locale.KOREA);
        final String INVALID_ACCESS_EXCEPTION_MSG_EN = getExceptionMsg(ExceptionAdvice.INVALID_ACCESS, Locale.ENGLISH);

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

    @Test
    @DisplayName("AuthenticationNumberTransferFailedException 검증")
    void AuthenticationNumberTransferFailedException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_KO = getExceptionCode(ExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.KOREA);
        final int AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_EN = getExceptionCode(ExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.ENGLISH);
        final String AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_KO = getExceptionMsg(ExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.KOREA);
        final String AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_EN = getExceptionMsg(ExceptionAdvice.AUTHENTICATION_NUMBER_TRANSFER_FAILED, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = exceptionAdvice.authenticationNumberTransferFailedException(new AuthenticationNumberTransferFailedException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = exceptionAdvice.authenticationNumberTransferFailedException(new AuthenticationNumberTransferFailedException());

        // Then
        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_KO, AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_EN);

        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_KO, commonResult_KO.getCode());
        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_CODE_EN, commonResult_EN.getCode());

        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_KO, commonResult_KO.getMassage());
        assertEquals(AUTHENTICATION_NUMBER_TRANSFER_FAILED_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }
}