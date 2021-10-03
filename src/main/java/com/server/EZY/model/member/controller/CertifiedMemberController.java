package com.server.EZY.model.member.controller;

import com.server.EZY.model.member.dto.AuthDto;
import com.server.EZY.model.member.dto.FcmTokenDto;
import com.server.EZY.model.member.dto.PhoneNumberChangeDto;
import com.server.EZY.model.member.dto.UsernameChangeDto;
import com.server.EZY.model.member.service.MemberService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.security.jwt.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "전화번호 변경", notes = "전화번호 변경")
    @ResponseStatus( HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult changePhoneNumber(@Valid @RequestBody PhoneNumberChangeDto phoneNumberChangeDto) {
        memberService.changePhoneNumber(phoneNumberChangeDto);
        return responseService.getSuccessResult();
    }

    /**
     * username 변경 controller
     * @param usernameChangeDto username, newUsername
     * @return SuccessResult
     */
    @PutMapping("/change/username")
    @ApiOperation(value = "이름 변경", notes = "이름 변경")
    @ResponseStatus( HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult changeUsername(@Valid @RequestBody UsernameChangeDto usernameChangeDto) {
        memberService.changeUsername(usernameChangeDto);
        return responseService.getSuccessResult();
    }

    /**
     * "/v1/member/logout"로 요청이 들어오고 (로그인이 되어있는 상태)를 확인했기 때문에 logout한다...
     * @param request HttpServletRequest
     * @return SuccessResult
     * @author 배태현
     */
    @DeleteMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃")
    @ResponseStatus( HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult logout(HttpServletRequest request) {
        memberService.logout(request.getRemoteUser());
        return responseService.getSuccessResult();
    }

    /**
     * "/v1/member/delete"로 요청이 들어와 로그인이 되어있는상태를 확인했기 때문에 회원탈퇴 진행
     * @param deleteUserDto
     * @return SuccessResult
     * @author 배태현
     */
    @PostMapping ("/delete")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴")
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult deleteUser(@Valid @RequestBody AuthDto deleteUserDto) {
        memberService.deleteUser(deleteUserDto);
        return responseService.getSuccessResult();
    }

    /**
     * fcmToken 변경 컨트롤러
     * @param fcmTokenDto
     * @return SuccessResult
     * @author 배태현
     */
    @PostMapping("/fcmtoken")
    @ApiOperation(value = "fcmToken 변경", notes = "fcmToken 변경")
    @ResponseStatus( HttpStatus.OK )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult updateFcmToken(@RequestBody FcmTokenDto fcmTokenDto) {
        memberService.updateFcmToken(fcmTokenDto);
        return responseService.getSuccessResult();
    }
}
