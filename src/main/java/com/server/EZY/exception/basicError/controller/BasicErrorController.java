package com.server.EZY.exception.basicError.controller;

import com.server.EZY.exception.basicError.BasicErrorHandler;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error/")
@RequiredArgsConstructor
public class BasicErrorController {

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @GetMapping(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME)
    public CommonResult unauthorized(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME);
    }

    @GetMapping(BasicErrorHandler.FORBIDDEN_403_CODE_NAME)
    public CommonResult forbidden(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.FORBIDDEN_403_CODE_NAME);
    }

    @GetMapping(BasicErrorHandler.NOT_FOUND_404_CODE_NAME)
    public CommonResult notFound(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.NOT_FOUND_404_CODE_NAME);
    }
}
