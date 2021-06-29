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

    /**
     * 로그인을 하지 않았을 때 전화번호 인증
     * @param phoneNumberDto phoneNumberDto
     * @return 전화번호 인증이 성공했을 때 true
     * @return 전화번호 인증이 실패했을 때 false
     * @author 배태현
     */
    @PostMapping("/phoneNumber")
    @ResponseStatus( HttpStatus.OK )
    public Boolean validPhoneNumber(@Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        return userService.validPhoneNumber(phoneNumberDto);
    }

    /**
     * 로그인을 하지 않았을 때 <br>
     * "/v1/phoneNumber"에서 전화번호 인증 후 <br>
     * 비밀번호를 변경하게하는 controller <br>
     * @param passwordChangeDto passwordChangeDto
     * @return (회원이름)회원 비밀번호 변경완료
     * @author 배태현
     */
    @PutMapping ("/pwd-change")
    @ResponseStatus( HttpStatus.OK )
    public String passwordChange(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return userService.changePassword(passwordChangeDto);
    }
}
