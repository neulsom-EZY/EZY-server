package com.server.EZY.model.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.user.controller.UserController;
import com.server.EZY.model.user.dto.LoginDto;
import com.server.EZY.model.user.dto.PasswordChangeDto;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("회원가입 테스트")
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
    @DisplayName("로그인 테스트")
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

    @Test
    @DisplayName("비밀번호 변경 테스트")
    public void pwdChangeTest() throws Exception {

        PasswordChangeDto passwordChangeDto = PasswordChangeDto.builder()
                .nickname("배태현")
                .currentPassword("1234")
                .newPassword("string")
                .build();

        String content = objectMapper.writeValueAsString(passwordChangeDto);

        final ResultActions actions = mvc.perform(put("/v1/member/pwd-change")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }
}