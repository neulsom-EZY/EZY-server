package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.token.TokenExceptionHandler;
import com.server.EZY.exception.token.exception.*;
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
@DisplayName("TokenExceptionAdvice test")
@AutoConfigureMockMvc
public class TokenExceptionHandlerTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
    @Autowired
    TokenExceptionHandler tokenExceptionHandler;

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



    @Test @DisplayName("AccessTokenException 검증")
    void AccessTokenException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int ACCESS_TOKEN_EXCEPTION_CODE_KO = getExceptionCode(TokenExceptionHandler.ACCESS_TOKEN_EXPIRED, Locale.KOREA);
        final int ACCESS_TOKEN_EXCEPTION_CODE_EN = getExceptionCode(TokenExceptionHandler.ACCESS_TOKEN_EXPIRED, Locale.ENGLISH);
        final String ACCESS_TOKEN_EXCEPTION_MSG_KO = getExceptionMsg(TokenExceptionHandler.ACCESS_TOKEN_EXPIRED, Locale.KOREA);
        final String ACCESS_TOKEN_EXCEPTION_MSG_EN = getExceptionMsg(TokenExceptionHandler.ACCESS_TOKEN_EXPIRED, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = tokenExceptionHandler.accessTokenExpiredException(new AccessTokenExpiredException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = tokenExceptionHandler.accessTokenExpiredException(new AccessTokenExpiredException());

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
        final int INVALID_EXCEPTION_CODE_KO = getExceptionCode(TokenExceptionHandler.INVALID_TOKEN, Locale.KOREA);
        final int INVALID_EXCEPTION_CODE_EN = getExceptionCode(TokenExceptionHandler.INVALID_TOKEN, Locale.ENGLISH);
        final String INVALID_EXCEPTION_MSG_KO = getExceptionMsg(TokenExceptionHandler.INVALID_TOKEN, Locale.KOREA);
        final String INVALID_EXCEPTION_MSG_EN = getExceptionMsg(TokenExceptionHandler.INVALID_TOKEN, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = tokenExceptionHandler.invalidTokenException(new InvalidTokenException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = tokenExceptionHandler.invalidTokenException(new InvalidTokenException());

        // Then
        assertEquals(INVALID_EXCEPTION_CODE_KO, INVALID_EXCEPTION_CODE_EN);

        assertEquals(INVALID_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(INVALID_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(INVALID_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(INVALID_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("TokenLoggedOutException 검증")
    void TokenLoggedOutException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int TOKEN_LOGGED_OUT_EXCEPTION_CODE_KO = getExceptionCode(TokenExceptionHandler.TOKEN_LOGGED_OUT, Locale.KOREA);
        final int TOKEN_LOGGED_OUT_EXCEPTION_CODE_EN = getExceptionCode(TokenExceptionHandler.TOKEN_LOGGED_OUT, Locale.ENGLISH);
        final String TOKEN_LOGGED_OUT_EXCEPTION_MSG_KO = getExceptionMsg(TokenExceptionHandler.TOKEN_LOGGED_OUT, Locale.KOREA);
        final String TOKEN_LOGGED_OUT_EXCEPTION_MSG_EN = getExceptionMsg(TokenExceptionHandler.TOKEN_LOGGED_OUT, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = tokenExceptionHandler.tokenLoggedOutException(new TokenLoggedOutException());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = tokenExceptionHandler.tokenLoggedOutException(new TokenLoggedOutException());

        // Then
        assertEquals(TOKEN_LOGGED_OUT_EXCEPTION_CODE_KO, TOKEN_LOGGED_OUT_EXCEPTION_CODE_EN);

        assertEquals(TOKEN_LOGGED_OUT_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(TOKEN_LOGGED_OUT_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(TOKEN_LOGGED_OUT_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(TOKEN_LOGGED_OUT_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("AuthorizationHeaderIsEmptyException 검증")
    void AuthorizationHeaderIsEmptyException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_CODE_KO = getExceptionCode(TokenExceptionHandler.AUTHORIZATION_HEADER_IS_EMPTY, Locale.KOREA);
        final int AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_CODE_EN = getExceptionCode(TokenExceptionHandler.AUTHORIZATION_HEADER_IS_EMPTY, Locale.ENGLISH);
        final String AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_MSG_KO = getExceptionMsg(TokenExceptionHandler.AUTHORIZATION_HEADER_IS_EMPTY, Locale.KOREA);
        final String AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_MSG_EN = getExceptionMsg(TokenExceptionHandler.AUTHORIZATION_HEADER_IS_EMPTY, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = tokenExceptionHandler.authorizationHeaderIsEmpty(new AuthorizationHeaderIsEmpty());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = tokenExceptionHandler.authorizationHeaderIsEmpty(new AuthorizationHeaderIsEmpty());

        // Then
        assertEquals(AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_CODE_KO, AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_CODE_EN);

        assertEquals(AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(AUTHORIZATION_HEADER_IS_EMPTY_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

    @Test @DisplayName("RefreshTokenHeaderIsEmptyException 검증")
    void RefreshTokenHeaderIsEmptyException_검증() throws Exception {
        // Given
        setLocal(Locale.KOREA);
        final int REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_CODE_KO = getExceptionCode(TokenExceptionHandler.REFRESH_TOKEN_HEADER_IS_EMPTY, Locale.KOREA);
        final int REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_CODE_EN = getExceptionCode(TokenExceptionHandler.REFRESH_TOKEN_HEADER_IS_EMPTY, Locale.ENGLISH);
        final String REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_MSG_KO = getExceptionMsg(TokenExceptionHandler.REFRESH_TOKEN_HEADER_IS_EMPTY, Locale.KOREA);
        final String REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_MSG_EN = getExceptionMsg(TokenExceptionHandler.REFRESH_TOKEN_HEADER_IS_EMPTY, Locale.ENGLISH);

        // When
        CommonResult commonResult_KO = tokenExceptionHandler.refreshTokenIsEmpty(new RefreshTokenHeaderIsEmpty());
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = tokenExceptionHandler.refreshTokenIsEmpty(new RefreshTokenHeaderIsEmpty());

        // Then
        assertEquals(REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_CODE_KO, REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_CODE_EN);

        assertEquals(REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(REFRESH_TOKEN_HEADER_IS_EMPTY_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }
}
