package com.server.EZY.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.user.controller.UserController;
import com.server.EZY.model.user.dto.LoginDto;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

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

        UserDto userDto = UserDto.builder()
                .nickname("JsonWebTok")
                .password("1234")
                .phoneNumber("01012345678")
                .build();
        userService.signup(userDto);
    }

    @Test
    public void signupTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .nickname("JsonWebTok")
                .password("1234")
                .phoneNumber("01012345678")
                .build();

        String content = objectMapper.writeValueAsString(userDto);

        final ResultActions actions = mvc.perform(post("/v1/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));


        actions
                .andDo(print())
                .andExpect(status().isCreated()); //http status 201 created
    }

    @Test
    public void signInTest() throws Exception {
        LoginDto loginDto = LoginDto.builder()
                .nickname("JsonWebTok")
                .password("1234")
                .build();

        String content = objectMapper.writeValueAsString(loginDto);

        final ResultActions actions = mvc.perform(post("/v1/signin")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));


        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }
}
