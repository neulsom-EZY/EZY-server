package com.server.EZY.controller;

import com.server.EZY.dto.LoginDto;
import com.server.EZY.dto.UserDto;
import com.server.EZY.repository.user.UserRepository;
import com.server.EZY.security.JwtTokenProvider;
import com.server.EZY.service.UserService;
import com.server.EZY.util.RedisUtil;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @PostMapping("/signup")
    public String signup(@ApiParam("Signup User") @RequestBody UserDto userDto) throws Exception {
        return userService.signup(userDto);
    }

    @PostMapping("/signin")
    public Map<String, String> signin(@Valid @RequestBody LoginDto loginDto) throws Exception {
        return userService.signin(loginDto);
    }
}
