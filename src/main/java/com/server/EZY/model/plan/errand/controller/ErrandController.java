package com.server.EZY.model.plan.errand.controller;

import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/errand")
@RequiredArgsConstructor
public class ErrandController {

    private final ResponseService responseService;

    @GetMapping("/{username}")
    public CommonResult getErrandAlarm(@PathVariable("username") String username) {
        return responseService.getSuccessResult();
    }

    @GetMapping("/errandIdx")
    public CommonResult thisErrandDetailSelect(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    @PostMapping("/send")
    public CommonResult sendErrand() {
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/recipient/{errandIdx}")
    public CommonResult deleteByReceiver(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }
}
