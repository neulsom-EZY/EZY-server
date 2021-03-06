package com.server.EZY.model.member.controller;

import com.server.EZY.model.member.dto.*;
import com.server.EZY.model.member.service.MemberService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.response.result.SingleResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 인증/인가 전 사용하는 회원 컨트롤러
 *
 * @version 1.0.0
 * @author 배태현
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final ResponseService responseService;

    /**
     * 이미 가입된 username인지 check 해주는 controller
     *
     * @param usernameDto username
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PostMapping("/verified/username")
    @ApiOperation(value = "username 존재 여부 확인", notes = "username 존재 여부 확인")
    @ResponseStatus( HttpStatus.OK )
    public SingleResult checkUsernameExist(@Valid @RequestBody UsernameDto usernameDto) {
        return responseService.getSingleResult(memberService.isExistUsername(usernameDto.getUsername()));
    }

    /**
     * 이미 가입된 phoneNumber인지 check 해주는 controller
     *
     * @param phoneNumberDto phoneNumber
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PostMapping("/verified/phone")
    @ApiOperation(value = "phoneNumber 존재 여부 확인", notes = "phoneNumber 존재 여부 확인")
    @ResponseStatus( HttpStatus.OK )
    public SingleResult checkPhoneNumberExist(@Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        return responseService.getSingleResult(memberService.isExistPhoneNumber(phoneNumberDto.getPhoneNumber()));
    }

    /**
     * 회원가입 controller
     *
     * @param memberDto userDto(username, password, phoneNumber, fcmToken)
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입")
    @ResponseStatus( HttpStatus.CREATED )
    public CommonResult signup(@Valid @RequestBody MemberDto memberDto) {
        memberService.signup(memberDto);
        return responseService.getSuccessResult();
    }

    /**
     * 로그인 controller
     *
     * @param loginDto loginDto(username, password)
     * @return SingleResult (username ,accessToken, refreshToken)
     * @author 배태현
     */
    @PostMapping("/signin")
    @ApiOperation(value = "로그인", notes = "로그인")
    @ResponseStatus( HttpStatus.OK )
    public SingleResult<Map<String, String>> signin(@Valid @RequestBody AuthDto loginDto) {
        Map<String, String> signinData = memberService.signin(loginDto);
        return responseService.getSingleResult(signinData);
    }

    /**
     * 전화번호로 인증번호를 전송하는 controller
     *
     * @param phoneNumberDto phoneNumber
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PostMapping("/auth")
    @ApiOperation(value = "전화번호로 인증번호 전송하기", notes = "전화번호로 인증번호 전송하기")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult sendAuthKey(@Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        memberService.sendAuthKey(phoneNumberDto.getPhoneNumber());
        return responseService.getSuccessResult();
    }

    /**
     * 받은 인증번호가 맞는지 인증하는 controller
     *
     * @param keyDto key
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PostMapping("/verified/auth")
    @ApiOperation(value = "인증번호 인증하기", notes = "인증번호 인증하기")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult validAuthKey(@Valid @RequestBody KeyDto keyDto) {
        memberService.validAuthKey(keyDto.getKey());
        return responseService.getSuccessResult();
    }

    /**
     * 비밀번호 재설정 전 회원정보, 인증번호를 전송하는 controller
     *
     * @param memberAuthKeySendInfoDto memberAuthKeySendInfoDto(username, newPassword)
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PostMapping ("/send/change/password/authkey")
    @ApiOperation(value = "비밀번호 재설정 전 정보, 인증번호 보내기", notes = "비밀번호 재설정 전 정보, 인증번호 보내기")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult sendAuthKeyByMemberInfo(@Valid @RequestBody MemberAuthKeySendInfoDto memberAuthKeySendInfoDto) {
        memberService.sendAuthKeyByMemberInfo(memberAuthKeySendInfoDto);
        return responseService.getSuccessResult();
    }

    /**
     * 인증번호 인증, 비밀번호 재설정 controller
     *
     * @param passwordChangeDto passwordChangeDto(key, username, newPassword)
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PutMapping("/change/password")
    @ApiOperation(value = "인증번호 인증, 비밀번호 재설정", notes = "인증번호 인증, 비밀번호 재설정")
    @ResponseStatus ( HttpStatus.OK )
    public CommonResult changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        memberService.changePassword(passwordChangeDto);
        return responseService.getSuccessResult();
    }

    /**
     * 문자로 username을 받는 컨트롤러
     *
     * @param phoneNumberDto phoneNumberDto(phoneNumber)
     * @return CommonResult - SuccessResult
     * @author 배태현
     */
    @PostMapping("/find/username")
    @ApiOperation(value = "username 찾기 (문자로 username 받기)", notes = "username 찾기 (문자로 username 받기)")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult findUsernameByPhoneNumber(@Valid @RequestBody PhoneNumberDto phoneNumberDto) {
        memberService.findUsernameByPhoneNumber(phoneNumberDto.getPhoneNumber());
        return responseService.getSuccessResult();
    }

    /**
     * keyword를 포함하고 있는 username을 찾아주는 컨트롤러
     *
     * @param keyword
     * @return getListResult()
     * @author 전지환
     */
    @GetMapping("/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult verifyUser(@RequestParam String keyword) {
        return responseService.getListResult(memberService.searchUser(keyword));
    }

}
