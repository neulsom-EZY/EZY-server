package com.server.EZY.model.member.controller;

import com.server.EZY.model.member.dto.AuthDto;
import com.server.EZY.model.member.dto.PasswordChangeDto;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.service.MemberService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.response.result.SingleResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final ResponseService responseService;

    /**
     * 회원가입 controller
     * @param memberDto userDto
     * @return SuccessResult
     * @throws Exception Exception
     * @author 배태현
     */
    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입")
    @ResponseStatus( HttpStatus.CREATED )
    public CommonResult signup(@Valid @RequestBody MemberDto memberDto) throws Exception {
        memberService.signup(memberDto);
        return responseService.getSuccessResult();
    }

    /**
     * 로그인 controller
     * @param loginDto loginDto
     * @return username ,accessToken, refreshToken
     * @throws Exception Exception
     * @author 배태현
     */
    @PostMapping("/signin")
    @ApiOperation(value = "로그인", notes = "로그인")
    @ResponseStatus( HttpStatus.OK )
    public SingleResult<Map<String, String>> signin(@Valid @RequestBody AuthDto loginDto) throws Exception {
        Map<String, String> signinData = memberService.signin(loginDto);
        return responseService.getSingleResult(signinData);
    }

    /**
     * 전화번호로 인증번호 보내기
     * @param phoneNumber
     * @return SuccessResult
     * @author 배태현
     */
    @PostMapping("/auth")
    @ApiOperation(value = "전화번호로 인증번호 보내기", notes = "전화번호로 인증번호 보내기")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult sendAuthKey(String phoneNumber) {
        memberService.sendAuthKey(phoneNumber);
        return responseService.getSuccessResult();
    }

    /**
     * 받은 인증번호가 맞는지 인증하기
     * @param key
     * @return SuccessResult
     * @author 배태현
     */
    @PostMapping("/auth/check")
    @ApiOperation(value = "인증번호 인증하기", notes = "인증번호 인증하기")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult validAuthKey(String key) {
        memberService.validAuthKey(key);
        return responseService.getSuccessResult();
    }

    /**
     * username을 찾는 controller
     * @param phoneNumber
     * @return SingleResult(찾은 회원이름)
     */
    @PostMapping("/find/username")
    @ApiOperation(value = "이름(아이디)찾기", notes = "이름(아이디)찾기")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult findUsername(String phoneNumber) {
        String username = memberService.findUsername(phoneNumber);
        return responseService.getSingleResult(username);
    }

    /**
     * 인증번호 인증을 한 뒤 <br>
     * 비밀번호를 변경하게하는 controller <br>
     * @param passwordChangeDto passwordChangeDto
     * @return SuccessResult
     * @author 배태현
     */
    @PutMapping ("/change/password")
    @ApiOperation(value = "비밀번호 찾기", notes = "비밀번호 찾기")
    @ResponseStatus( HttpStatus.OK )
    public CommonResult passwordChange(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        memberService.changePassword(passwordChangeDto);
        return responseService.getSuccessResult();
    }
}
