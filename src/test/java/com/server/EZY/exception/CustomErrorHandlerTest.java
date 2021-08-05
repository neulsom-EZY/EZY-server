package com.server.EZY.exception;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest @DisplayName("ExceptionAdvice test")
@AutoConfigureMockMvc
class CustomErrorHandlerTest {

    @Autowired ObjectMapper objMapper;
    @Autowired MessageSource messageSource;
}