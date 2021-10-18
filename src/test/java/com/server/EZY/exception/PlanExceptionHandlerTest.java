package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.plan.PlanExceptionHandler;
import com.server.EZY.exception.plan.exception.PlanNotFoundException;
import com.server.EZY.exception.user.MemberExceptionHandler;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.model.plan.enum_type.PlanType;
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
@DisplayName("PlanException test")
@AutoConfigureMockMvc
public class PlanExceptionHandlerTest {

    @Autowired
    ObjectMapper objMapper;
    @Autowired
    MessageSource messageSource;
    @Autowired
    PlanExceptionHandler planExceptionHandler;

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
    @DisplayName("PlanNotFoundException 검증")
    void PlanNotFoundException_검증() throws Exception {
        // Given
        String planType = PlanType.심부름.name();

        setLocal(Locale.KOREA);
        final int PLAN_NOT_FOUND_EXCEPTION_CODE_KO = getExceptionCode(PlanExceptionHandler.PLAN_NOT_FOUND, Locale.KOREA);
        final int PLAN_NOT_FOUND_EXCEPTION_CODE_EN = getExceptionCode(PlanExceptionHandler.PLAN_NOT_FOUND, Locale.ENGLISH);
        final String PLAN_NOT_FOUND_EXCEPTION_MSG_KO = getExceptionMsg(PlanExceptionHandler.PLAN_NOT_FOUND, Locale.KOREA)
                .replace(":planType", planType);
        final String PLAN_NOT_FOUND_EXCEPTION_MSG_EN = getExceptionMsg(PlanExceptionHandler.PLAN_NOT_FOUND, Locale.ENGLISH)
                .replace(":planType", planType);

        // When
        CommonResult commonResult_KO = planExceptionHandler.planNotFoundException(new PlanNotFoundException(PlanType.심부름));
        setLocal(Locale.ENGLISH);
        CommonResult commonResult_EN = planExceptionHandler.planNotFoundException(new PlanNotFoundException(PlanType.심부름));

        // Then
        assertEquals(PLAN_NOT_FOUND_EXCEPTION_CODE_KO, PLAN_NOT_FOUND_EXCEPTION_CODE_EN);

        assertEquals(PLAN_NOT_FOUND_EXCEPTION_CODE_KO, commonResult_KO.getCode());
        assertEquals(PLAN_NOT_FOUND_EXCEPTION_CODE_EN, commonResult_EN.getCode());

        assertEquals(PLAN_NOT_FOUND_EXCEPTION_MSG_KO, commonResult_KO.getMassage());
        assertEquals(PLAN_NOT_FOUND_EXCEPTION_MSG_EN, commonResult_EN.getMassage());

        printResult(commonResult_KO, commonResult_EN);
    }

}