package com.server.EZY.model.user.controller;

import com.server.EZY.model.user.dto.LoginDto;
import com.server.EZY.model.user.dto.PasswordChangeDto;
import com.server.EZY.model.user.dto.PhoneNumberDto;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import com.server.EZY.model.user.service.UserService;
import com.server.EZY.util.RedisUtil;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus( HttpStatus.CREATED )
    public String signup(@ApiParam("Signup User") @RequestBody UserDto userDto) throws Exception {
        return userService.signup(userDto);
    }

    @PostMapping("/signin")
    @ResponseStatus( HttpStatus.OK )
    public Map<String, String> signin(@Valid @RequestBody LoginDto loginDto) throws Exception {
        return userService.signin(loginDto);
    }
}
