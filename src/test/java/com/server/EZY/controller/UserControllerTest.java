package com.server.EZY.controller;

import com.server.EZY.dto.UserDto;
import com.server.EZY.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void signupTest() {
        //given
        UserDto userDto = new UserDto();
        userDto.setNickname("JsonWebTok");
        userDto.setPassword("1234");
        userDto.setPhoneNumber("01012341234");

        //when
        String token = userService.signup(userDto); //password size shit!!

        //then
        System.out.println("signup result = " + token);
    }
}
