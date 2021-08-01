package com.server.EZY.exception.authenticationNumber.controller;

import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/exception")
public class AuthenticationNumberExceptionController {

    @GetMapping("/invalid-authentication-number")
    public CommonResult invalidAuthenticationNumber(){ throw new InvalidAuthenticationNumberException(); }

    @GetMapping("/authentication-number-transfer-failed")
    public CommonResult authenticationNumberTransferFailed(){ throw new AuthenticationNumberTransferFailedException(); }

}
