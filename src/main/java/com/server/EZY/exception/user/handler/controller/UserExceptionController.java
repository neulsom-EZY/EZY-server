package com.server.EZY.exception.user.handler.controller;

import com.server.EZY.exception.user.UserNotFoundException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/exception")
public class UserExceptionController {

    @GetMapping("/user-not-found")
    public CommonResult userNotFoundException(){
        throw new UserNotFoundException();
    }
}
