package com.server.EZY.model.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.user.dto.*;
import com.server.EZY.model.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CertifiedUserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @BeforeEach
    public void before(@Autowired CertifiedUserController certifiedUserController) {
        mvc = MockMvcBuilders.standaloneSetup(certifiedUserController).build();

        UserDto userDto = UserDto.builder()
                .nickname("JsonWebTok")
                .password("1234")
                .phoneNumber("01012345678")
                .build();
        userService.signup(userDto);
    }

    @Test
    public void validPhoneNumber() throws Exception {
        PhoneNumberDto phoneNumberDto = PhoneNumberDto.builder()
                .phoneNumber("01012341234")
                .build();

        String content = objectMapper.writeValueAsString(phoneNumberDto);

        final ResultActions actions = mvc.perform(post("/v1/user/phoneNumber")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));


        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }

    @Test
    public void passwordChange() throws Exception {
        PasswordChangeDto passwordChangeDto = PasswordChangeDto.builder()
                .nickname("배태현")
                .currentPassword("1234")
                .newPassword("string")
                .build();

        String content = objectMapper.writeValueAsString(passwordChangeDto);

        final ResultActions actions = mvc.perform(put("/v1/user/pwd-change")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }

    @Test
    public void logoutTest() throws Exception {

        final ResultActions actions = mvc.perform(delete("/v1/user/logout")
                .content("content")
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isOk()); //http status 200
    }
    
    @Test
    public void withdrawalTest() throws Exception {

        WithdrawalDto withdrawalDto = WithdrawalDto.builder()
                .nickname("배태현")
                .password("1234")
                .build();

        String content = objectMapper.writeValueAsString(withdrawalDto);

        final ResultActions actions = mvc.perform(get("/v1/user/withdrawal")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andDo(print())
                .andExpect(status().isNoContent()); //http status 204
    }
}