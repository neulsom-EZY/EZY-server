package com.server.EZY.model.user.controller;

import com.server.EZY.model.user.dto.LoginDto;
import com.server.EZY.model.user.dto.PasswordChangeDto;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.service.UserService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/member")
public class UserController {
    private final UserService userService;

    /**
     * 회원가입 controller
     * @param userDto userDto
     * @return accessToken
     * @throws Exception Exception
     * @author 배태현
     */
    @PostMapping("/signup")
    @ResponseStatus( HttpStatus.CREATED )
    public String signup(@ApiParam("Signup User") @RequestBody UserDto userDto) throws Exception {
        return userService.signup(userDto);
    }

    /**
     * 로그인 controller
     * @param loginDto loginDto
     * @return nickname ,accessToken, refreshToken
     * @throws Exception Exception
     * @author 배태현
     */
    @PostMapping("/signin")
    @ResponseStatus( HttpStatus.OK )
    public Map<String, String> signin(@Valid @RequestBody LoginDto loginDto) throws Exception {
        return userService.signin(loginDto);
    }

    @PostMapping("/change/username")
    @ResponseStatus( HttpStatus.OK )
    public String changeUsername() {
        return null;
    }

    /**
     * 전화번호로 인증번호 보내기
     * @param phoneNumber
     * @return true or false
     * @author 배태현
     */
    @PostMapping("/auth")
    @ResponseStatus( HttpStatus.OK )
    public String sendAuthKey(String phoneNumber) {
        return userService.sendAuthKey(phoneNumber);
    }

    /**
     * 받은 인증번호가 맞는지 인증하기
     * @param key
     * @return (username) + 님 휴대전화 인증 완료
     * @author 배태현
     */
    @PostMapping("/auth/check")
    @ResponseStatus( HttpStatus.OK )
    public String validAuthKey(String key) {
        return userService.validAuthKey(key);
    }

    /**
     * 인증번호 인증을 한 뒤 <br>
     * 비밀번호를 변경하게하는 controller <br>
     * @param passwordChangeDto passwordChangeDto
     * @return (회원이름)회원 비밀번호 변경완료
     * @author 배태현
     */
    @PutMapping ("/change/password")
    @ResponseStatus( HttpStatus.OK )
    public String passwordChange(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return userService.changePassword(passwordChangeDto);
    }
}
