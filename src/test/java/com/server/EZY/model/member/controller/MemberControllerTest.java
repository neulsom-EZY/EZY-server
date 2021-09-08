package com.server.EZY.model.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.member.dto.AuthDto;
import com.server.EZY.model.member.dto.PasswordChangeDto;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.dto.UsernameChangeDto;
import com.server.EZY.model.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    //signinTest를 위해 만들어진 before
    @BeforeEach
    public void before(@Autowired MemberController memberController) {
        mvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void signupTest() throws Exception {
        MemberDto memberDto = MemberDto.builder()
                .username("@bBbB")
                .password("1234")
                .phoneNumber("01008090809")
                .build();

        String content = objectMapper.writeValueAsString(memberDto);

        final ResultActions actions = mvc.perform(post("/v1/member/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));


        actions
                .andDo(print())
                .andExpect(status().isCreated()); //http status 201 created
    }

    @Test
    @DisplayName("로그인 테스트")
    public void signInTest() throws Exception {
        AuthDto loginDto = AuthDto.builder()
                .username("@Json")
                .password("1234")
                .build();

        String content = objectMapper.writeValueAsString(loginDto);

        final ResultActions actions = mvc.perform(post("/v1/member/signin")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));


        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }

    @Test
    @DisplayName("인증번호 보내기 테스트")
    public void sendAuthKey() throws Exception {

        String content = objectMapper.writeValueAsString("01049977055");

        final ResultActions actions = mvc.perform(post("/v1/member/auth")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }

    @Test
    @DisplayName("인증번호 인증 테스트")
    public void validAuthKey() throws Exception{

        String content = objectMapper.writeValueAsString("0000");

        final ResultActions actions = mvc.perform(post("/v1/member/auth/check")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }

    @Test
    @DisplayName("username 찾기 테스트")
    public void findUsernameTest() throws Exception {

        String content = objectMapper.writeValueAsString("01012341234");

        final ResultActions actions = mvc.perform(post("/v1/member/find/username")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    public void pwdChangeTest() throws Exception {

        PasswordChangeDto passwordChangeDto = PasswordChangeDto.builder()
                .username("@Baeeeee")
                .newPassword("string")
                .build();

        String content = objectMapper.writeValueAsString(passwordChangeDto);

        final ResultActions actions = mvc.perform(put("/v1/member/change/password")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }
}