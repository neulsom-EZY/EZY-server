package com.server.EZY.model.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.member.dto.*;
import com.server.EZY.model.member.service.MemberService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CertifiedMemberControllerTest {

    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @BeforeEach
    public void before(@Autowired CertifiedMemberController certifiedMemberController) {
        mvc = MockMvcBuilders.standaloneSetup(certifiedMemberController).build();
    }

    @Test
    @DisplayName("로그아웃 테스트")
    public void logoutTest() throws Exception {

        final ResultActions actions = mvc.perform(delete("/v1/member/logout")
                .content("content")
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }
    
    @Test
    @DisplayName("회원탈퇴 테스트")
    public void deleteMemberTest() throws Exception {

        AuthDto deleteUserDto = AuthDto.builder()
                .username("@BaeTul")
                .password("1234")
                .build();

        String content = objectMapper.writeValueAsString(deleteUserDto);

        final ResultActions actions = mvc.perform(post("/v1/member/delete")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isNoContent()); //http status 204
    }
}