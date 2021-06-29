package com.server.EZY.model.user.controller;

import com.server.EZY.model.user.dto.PasswordChangeDto;
import com.server.EZY.model.user.dto.PhoneNumberDto;
import com.server.EZY.model.user.dto.WithdrawalDto;
import com.server.EZY.model.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/user") //로그인이 되어있고 Role type이 "ROLE_CLIENT"인 유저만 접근할 수 있다
public class CertifiedUserController {

    private final UserService userService;

    /**
     * 로그인이 되어있을 때 휴대폰인증을 하는 로직
     * @param phoneNumberDto phoneNumberDto
     * @return (true || false)
     * @author 배태현
     */
    @PostMapping("/phoneNumber")
    @ResponseStatus( HttpStatus.OK )
    public Boolean validPhoneNumber(@Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        return userService.validPhoneNumber(phoneNumberDto);
    }

    /**
     * 로그인을 하였어도 <br>
     * "/v1/user/phoneNumber"에서 전화번호 인증 후 <br>
     * 비밀번호를 변경하게하는 controller <br>
     * @param passwordChangeDto passwordChangeDto
     * @return (회원이름)회원 비밀번호 변경완료
     * @author 배태현
     */
    @PutMapping("/pwd-change")
    @ResponseStatus( HttpStatus.OK )
    public String changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        return userService.changePassword(passwordChangeDto);
    }

    /**
     * "/v1/user/logout"로 요청이 들어오고 권한이 이미 확인된 상태(로그인이 되어있는 상태)를 확인했기 때문에 logout한다...
     * @param request HttpServletRequest
     * @return "로그아웃 되었습니다."
     * @author 배태현
     */
    @DeleteMapping("/logout")
    @ResponseStatus( HttpStatus.OK )
    public String logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    /**
     * "/v1/user/withdrawal"로 요청이 들어와 로그인이 되어있는상태, 권환이 확인된 상태이기 때문에 회원탈퇴 진행
     * @param withdrawalDto withdrawalDto
     * @return "(회원이름)회원 회원탈퇴완료"
     * @author 배태현
     */
    @GetMapping ("/withdrawal")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public String withdrawal(@Valid @RequestBody WithdrawalDto withdrawalDto) {
        return userService.withdrawal(withdrawalDto);
    }
}
