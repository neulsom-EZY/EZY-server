package com.server.EZY.model.user.controller;

import com.server.EZY.model.user.dto.DeleteUserDto;
import com.server.EZY.model.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/member")
public class CertifiedUserController {

    private final UserService userService;

    /**
     * "/v1/member/logout"로 요청이 들어오고 (로그인이 되어있는 상태)를 확인했기 때문에 logout한다...
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
     * "/v1/member/delete"로 요청이 들어와 로그인이 되어있는상태를 확인했기 때문에 회원탈퇴 진행
     * @param deleteUserDto
     * @return "(회원이름)회원 회원탈퇴완료"
     * @author 배태현
     */
    @GetMapping ("/delete")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    public String deleteUser(@Valid @RequestBody DeleteUserDto deleteUserDto) {
        return userService.deleteUser(deleteUserDto);
    }
}
