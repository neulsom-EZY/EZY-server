package com.server.EZY.exception.basicError.controller;

import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
@RequiredArgsConstructor
public class BasicErrorController {

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    final String CUSTOM_401_UNAUTHORIZED = "unauthorized";
    final String CUSTOM_403_FORBIDDEN = "forbidden";
    final String CUSTOM_404_NOT_FOUND = "not-found";

    @GetMapping("/unauthorized")
    public CommonResult unauthorized(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_401_UNAUTHORIZED);
    }

    @GetMapping("/not-found")
    public CommonResult notFound(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_404_NOT_FOUND);
    }

    @GetMapping("/forbidden")
    public CommonResult forbidden(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_403_FORBIDDEN);
    }
}
