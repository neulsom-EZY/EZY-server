package com.server.EZY.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.EzyApplication;
import com.server.EZY.dto.LoginDto;
import com.server.EZY.dto.UserDto;
import com.server.EZY.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@Slf4j
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    //signinTest를 위해 만들어진 before
    @BeforeEach
    public void before(@Autowired UserController userController) {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();

        UserDto userDto = new UserDto("JsonWebTok", "1234", "01012345678");
        userService.signup(userDto);
    }

    @Test
    public void signupTest() throws Exception {
        UserDto userDto = new UserDto("JsonWebTok", "1234", "01012341234");

        String content = objectMapper.writeValueAsString(userDto);

        final ResultActions actions = mvc.perform(post("/v1/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));


        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }

    @Test
    public void signInTest() throws Exception {
        //given
        LoginDto loginDto = new LoginDto("BeforeEach", "12345");

        String content = objectMapper.writeValueAsString(loginDto);

        final ResultActions actions = mvc.perform(post("/v1/signin")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));


        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }
}
