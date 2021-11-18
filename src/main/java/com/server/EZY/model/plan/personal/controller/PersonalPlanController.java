package com.server.EZY.model.plan.personal.controller;

import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.service.PersonalPlanService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/v1/plan/personal")
@RequiredArgsConstructor
public class PersonalPlanController {
    private final PersonalPlanService personalPlanService;
    private final ResponseService responseService;

    /**
     * 개인 일정을 추가하는 controller.
     *
     * @param personalPlanSetDto
     * @return getSuccessResult or NOT
     * @author 전지환
     */
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult savePersonalPlan(@RequestBody PersonalPlanDto.PersonalPlanSet personalPlanSetDto) {
        personalPlanService.createPersonalPlan(personalPlanSetDto);
        return responseService.getSuccessResult();
    }

    /**
     * 기간별 개인 일정을 조회하는 controller.
     *
     * @param startDateOrNull
     * @param endDateOrNull
     * @return ListResult
     * @author 전지환
     */
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult getAllPersonalPlan(
            // TODO Exception Handling
            @RequestParam(value = "startDate", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDateOrNull,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDateOrNull
    ){
        log.debug("====it's startDateOrNull======{}======", startDateOrNull);
        log.debug("=====it's endDateOrNull====={}======", endDateOrNull);
        /**
         * if - 사용자가 startDate 와 endDate를 모두 기입했을 때, 기간 내에 수행되는 개인일정 모두 조회
         * else if - 사용자가 startDate 만 기입을 했을 때, 해당 날짜에 수행되는 개인일정 모두 조회
         * else - 사용자가 param으로 넘겨준 값이 없을 때, 사용자의 모든 개인일정 조회
         */
        if (startDateOrNull!=null && endDateOrNull != null){
            return responseService.getListResult(personalPlanService.getPersonalPlanEntitiesBetween(startDateOrNull, endDateOrNull));
        } else if(startDateOrNull != null){
            return responseService.getListResult(personalPlanService.getThisDatePersonalPlanEntities(startDateOrNull));
        } else{
            return responseService.getListResult(personalPlanService.getAllPersonalPlan());
        }
    }

    /**
     * 개인 일정 단건 조회하는 controller.
     *
     * @param planIdx
     * @return getSingleResult
     * @author 전지환
     */
    @GetMapping("/{planIdx}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult getThisPersonalPlan(@PathVariable("planIdx") Long planIdx){
        return responseService.getSingleResult(personalPlanService.getThisPersonalPlan(planIdx));
    }

    /**
     * 특정 개인 일정을 수정하는 controller.
     *
     * @param planIdx
     * @param personalPlanSetDto
     * @return getSuccessResult
     * @throws Exception
     * @author 전지환
     */
    @PutMapping("/{planIdx}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult updateThisPersonalPlan(@PathVariable Long planIdx, @RequestBody PersonalPlanDto.PersonalPlanSet personalPlanSetDto) throws Exception {
        personalPlanService.updateThisPersonalPlan(planIdx, personalPlanSetDto);
        return responseService.getSuccessResult();
    }


    /**
     * 특정 개인 일정을 삭제하는 controller.
     *
     * @param planIdx
     * @return getSuccessResult
     * @author 전지환
     */
    @DeleteMapping("/{planIdx}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult deleteThisPersonalPlan(@PathVariable Long planIdx){
        personalPlanService.deleteThisPersonalPlan(planIdx);
        return responseService.getSuccessResult();
    }
}
