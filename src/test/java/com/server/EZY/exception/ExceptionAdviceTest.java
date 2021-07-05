package com.server.EZY.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.exception.user.UserExceptionController;
import com.server.EZY.exception.user.exception.UserNotFoundException;
import com.server.EZY.response.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest @DisplayName("ExceptionAdvice test")
class ExceptionAdviceTest {

    MockMvc mockMvc;
    ResultActions resultActions;
    @Autowired ExceptionAdvice exceptionAdvice;
    @Autowired UserExceptionController userExceptionController;

    @AfterEach
    void resultActionsReset(){
        resultActions = null;
    }

    String objectToJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    @Test @DisplayName("만약 UserNotFoundException이 발생한다면?")
    void UserNotFoundException_검증() throws Exception {
        // Given
        mockMvc = MockMvcBuilders.standaloneSetup(userExceptionController)
                .setControllerAdvice(exceptionAdvice)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
                .build();
        String userNotFoundExceptionResult = objectToJson(exceptionAdvice.userNotFoundException(new UserNotFoundException()));

        // When
        resultActions = mockMvc.perform(get("/exception/user-not-found"));

        // Then
        resultActions
                .andExpect(status().isNotFound());
        log.info("=== UserNotFoundException Response ===\n{}", userNotFoundExceptionResult);
    }

}