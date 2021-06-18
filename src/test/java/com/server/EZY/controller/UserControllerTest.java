package com.server.EZY.controller;

import com.server.EZY.dto.LoginDto;
import com.server.EZY.dto.UserDto;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.repository.user.UserRepository;
import com.server.EZY.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class UserControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void signupTest() {
        //given
        UserDto userDto = new UserDto("JsonWebTok", "asdfasd", "01012341234");

        //when
        String token = userService.signup(userDto);

        //then
        log.info("signup result = " + token);
        assertEquals(true, token!=null);
    }

    //signinTest를 위해 만들어진 before
    @BeforeEach
    public void before() {
        UserDto userDto = new UserDto();
        userDto.setNickname("BeforeEach");
        userDto.setPassword("12345");
        userDto.setPhoneNumber("01012345678");

        userService.signup(userDto);
    }

    @Test
    public void signInTest() {
        //given
        LoginDto loginDto = new LoginDto();
        loginDto.setNickname("BeforeEach");
        loginDto.setPassword("12345");
        //when
        Map<String, String> signInResult = userService.signin(loginDto);
        //then
        for (String s : signInResult.values()) {
            log.info("s = " + s);
            assertEquals(true, s!=null);
        }
    }
}
