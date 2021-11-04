package com.server.EZY.model.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.member.dto.AuthDto;
import com.server.EZY.model.member.dto.MemberAuthKeySendInfoDto;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.dto.PhoneNumberDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class MemberControllerTest {

    @Autowired private ObjectMapper objectMapper;

    private MockMvc mvc;

    //signinTest를 위해 만들어진 before
    @BeforeEach
    public void before(@Autowired MemberController memberController) {
        mvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    public void sendUsernameTest() throws Exception {
        //given
        PhoneNumberDto phoneNumberDto = PhoneNumberDto.builder()
                .phoneNumber("01008090809")
                .build();

        String content = objectMapper.writeValueAsString(phoneNumberDto);

        //when
        final ResultActions actions = mvc.perform(post("/v1/member/find/username")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("회원가입 테스트")
    public void signupTest() throws Exception {
        MemberDto memberDto = MemberDto.builder()
                .username("@bBbB")
                .password("qwerqwer")
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
                .password("1234qwer")
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

        final ResultActions actions = mvc.perform(post("/v1/member/verified/auth")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }

    @Test
    @DisplayName("비밀번호 변경 전 정보, 인증번호 전송 테스트")
    public void pwdInfoTest() throws Exception {

        MemberAuthKeySendInfoDto memberAuthKeySendInfoDto = MemberAuthKeySendInfoDto.builder()
                .username("@Baeeeee")
                .phoneNumber("01049977055")
                .build();

        String content = objectMapper.writeValueAsString(memberAuthKeySendInfoDto);

        final ResultActions actions = mvc.perform(post("/v1/member/send/change/password/authkey")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }
}