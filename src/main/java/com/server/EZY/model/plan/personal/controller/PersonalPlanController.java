package com.server.EZY.model.plan.personal.controller;

import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;
import com.server.EZY.model.plan.personal.service.PersonalPlanService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/plan")
@RequiredArgsConstructor
public class PersonalPlanController {
    private final PersonalPlanService personalPlanService;
    private final ResponseService responseService;

    @PostMapping("/personal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult savePersonalPlan(@RequestBody PersonalPlanSetDto personalPlanSetDto) {
        personalPlanService.createPersonalPlan(personalPlanSetDto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/personal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult getAllPersonalPlan(){
        return responseService.getListResult(personalPlanService.getAllPersonalPlan());
    }
}
