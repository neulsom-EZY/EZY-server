package com.server.EZY.exception.custom;

import com.server.EZY.response.result.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class CustomExceptionController {

    public CommonResult exception() throws Exception {
        throw new Exception();
    }
}
