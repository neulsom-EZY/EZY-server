package com.server.EZY.model.plan.errand.controller;

import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/errand")
@RequiredArgsConstructor
public class ErrandStatusCycleController {

    private final ResponseService responseService;

    @GetMapping("/check/{errandIdx}")
    public CommonResult readThisSchedule(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    @PutMapping("/accept/{errandIdx}")
    public CommonResult acceptErrand(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    @PutMapping("/reject/{errandIdx}")
    public CommonResult rejectErrand(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    @PutMapping("/complete/{errandIdx}")
    public CommonResult completeErrand(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    @PutMapping("/fail/{errandIdx}")
    public CommonResult failErrand(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }
}
