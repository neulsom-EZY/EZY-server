package com.server.EZY.exception.authentication_number.controller;

import com.server.EZY.exception.authentication_number.exception.FailedToSendMessageException;
import com.server.EZY.exception.authentication_number.exception.InvalidAuthenticationNumberException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/exception")
public class AuthenticationNumberExceptionController {

    @GetMapping("/invalid-authentication-number")
    public CommonResult invalidAuthenticationNumber(){ throw new InvalidAuthenticationNumberException(); }

    @GetMapping("/fail-to-send-message")
    public CommonResult failToSendMessage(){ throw new FailedToSendMessageException(); }

}
