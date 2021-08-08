package com.server.EZY.model.member.controller;

import com.server.EZY.model.member.dto.AuthDto;
import com.server.EZY.model.member.dto.PhoneNumberChangeDto;
import com.server.EZY.model.member.service.MemberService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/member")
public class CertifiedMemberController {

    private final MemberService memberService;
    private final ResponseService responseService;

    //이 Controller가 return이 아무값도 return되지않음 공백이 뜸 (POSTMAN에서 확인)


    /**
     * (로그인 되어있는 상태에서) 전화번호 인증을 완료하고 전화번호를 변경할 수 있음
     * 전화번호를 변경하는 controller
     * @param phoneNumberChangeDto
     * @return SuccessResult
     * @author 배태현
     */
    @PutMapping("/change/phone")
    @ResponseStatus( HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult changePhoneNumber(@RequestBody PhoneNumberChangeDto phoneNumberChangeDto) {
        memberService.changePhoneNumber(phoneNumberChangeDto);
        return responseService.getSuccessResult();
    }

    /**
     * "/v1/member/logout"로 요청이 들어오고 (로그인이 되어있는 상태)를 확인했기 때문에 logout한다...
     * @param request HttpServletRequest
     * @return SuccessResult
     * @author 배태현
     */
    @DeleteMapping("/logout")
    @ResponseStatus( HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult logout(HttpServletRequest request) {
        memberService.logout(request);
        return responseService.getSuccessResult();
    }

    /**
     * "/v1/member/delete"로 요청이 들어와 로그인이 되어있는상태를 확인했기 때문에 회원탈퇴 진행
     * @param deleteUserDto
     * @return SuccessResult
     * @author 배태현
     */
    @PostMapping ("/delete")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult deleteUser(@Valid @RequestBody AuthDto deleteUserDto) {
        memberService.deleteUser(deleteUserDto);
        return responseService.getSuccessResult();
    }
}
