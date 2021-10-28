package com.server.EZY.exception.basic_error.controller;

import com.server.EZY.exception.basic_error.BasicErrorHandler;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BasicErrorHandler.BASIC_ERROR_BASE_URL)
@RequiredArgsConstructor
public class BasicErrorController {

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @GetMapping(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME)
    public CommonResult getUnauthorized(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME);
    }

    @GetMapping(BasicErrorHandler.FORBIDDEN_403_CODE_NAME)
    public CommonResult getForbidden(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.FORBIDDEN_403_CODE_NAME);
    }

    @GetMapping(BasicErrorHandler.NOT_FOUND_404_CODE_NAME)
    public CommonResult getNotFound(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.NOT_FOUND_404_CODE_NAME);
    }

    @PostMapping(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME)
    public CommonResult postUnauthorized(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME);
    }

    @PostMapping(BasicErrorHandler.FORBIDDEN_403_CODE_NAME)
    public CommonResult postForbidden(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.FORBIDDEN_403_CODE_NAME);
    }

    @PostMapping(BasicErrorHandler.NOT_FOUND_404_CODE_NAME)
    public CommonResult postNotFound(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.NOT_FOUND_404_CODE_NAME);
    }

    @PutMapping(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME)
    public CommonResult putUnauthorized(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME);
    }

    @PutMapping(BasicErrorHandler.FORBIDDEN_403_CODE_NAME)
    public CommonResult putForbidden(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.FORBIDDEN_403_CODE_NAME);
    }

    @PutMapping(BasicErrorHandler.NOT_FOUND_404_CODE_NAME)
    public CommonResult putNotFound(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.NOT_FOUND_404_CODE_NAME);
    }

    @DeleteMapping(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME)
    public CommonResult deleteUnauthorized(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.UNAUTHORIZED_401_CODE_NAME);
    }

    @DeleteMapping(BasicErrorHandler.FORBIDDEN_403_CODE_NAME)
    public CommonResult deleteForbidden(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.FORBIDDEN_403_CODE_NAME);
    }

    @DeleteMapping(BasicErrorHandler.NOT_FOUND_404_CODE_NAME)
    public CommonResult deleteNotFound(){
        return exceptionResponseObjectUtil.getExceptionResponseObj(BasicErrorHandler.NOT_FOUND_404_CODE_NAME);
    }
}
