package com.server.EZY.model.plan.personal.controller;

import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;
import com.server.EZY.model.plan.personal.service.PersonalPlanService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/plan")
@RequiredArgsConstructor
public class PersonalPlanController {
    private final PersonalPlanService personalPlanService;
    private final ResponseService responseService;

    @PostMapping("/personal")
    public CommonResult savePersonalPlan(@RequestBody PersonalPlanSetDto personalPlanSetDto) {
        personalPlanService.createPersonalPlan(personalPlanSetDto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/personal")
    public CommonResult getAllPersonalPlan(){
        return responseService.getListResult(personalPlanService.getAllPersonalPlan());
    }
}
