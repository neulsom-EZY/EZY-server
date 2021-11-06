package com.server.EZY.model.plan.errand.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.exception.plan.exception.PlanNotFoundException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.model.plan.errand.service.ErrandService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/errand")
@RequiredArgsConstructor
public class ErrandStatusCycleController {

    private final ResponseService responseService;
    private final ErrandService errandService;

    /**
     * 심부름 수락 Controller
     *
     * @param errandIdx 수락할 심부름의 인덱스(planIdx)
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws PlanNotFoundException  해당 심부름이 존재하지 않을 때
     * @return SuccessResult - 심부름 거절 성공시
     * @author 배태현, 정시원
     */
    @PutMapping("/accept/{errandIdx}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
    })
    public CommonResult acceptErrand(@PathVariable("errandIdx") long errandIdx) throws FirebaseMessagingException {
        errandService.acceptErrand(errandIdx);
        return responseService.getSuccessResult();
    }

    /**
     * 심부름 거절 Controller
     *
     * @param errandIdx 거절할 심부름의 인덱스(planIdx)
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws PlanNotFoundException  해당 심부름이 존재하지 않을 때
     * @return SuccessResult - 심부름 거절 성공시
     * @author 배태현, 정시원
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
    })
    @PutMapping("/reject/{errandIdx}")
    public CommonResult rejectErrand(@PathVariable("errandIdx") long errandIdx) throws FirebaseMessagingException {
        errandService.refuseErrand(errandIdx);
        return responseService.getSuccessResult();
    }

    /**
     * 심부름 완료 Controller
     * @param errandIdx
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws PlanNotFoundException  해당 심부름이 존재하지 않을 때
     * @return SuccessResult - 심부름 성공 성공시
     * @author 배태현, 정시원
     */
    @PutMapping("/complete/{errandIdx}")
    public CommonResult completeErrand(@PathVariable("errandIdx") Long errandIdx) throws FirebaseMessagingException {
        errandService.completionErrand(errandIdx);
        return responseService.getSuccessResult();
    }


    /**
     * 심부름 실패 Controller
     * @param errandIdx
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws PlanNotFoundException  해당 심부름이 존재하지 않을 때
     * @return SuccessResult - 심부름 포기 성공시
     * @author 정시원
     */
    @PutMapping("/fail/{errandIdx}")
    public CommonResult failErrand(@PathVariable("errandIdx") Long errandIdx) throws FirebaseMessagingException {
        errandService.failErrand(errandIdx);
        return responseService.getSuccessResult();
    }

    /**
     * 심부름 포기 Controller
     * @param errandIdx
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws PlanNotFoundException  해당 심부름이 존재하지 않을 때
     * @return SuccessResult - 심부름 포기 성공시
     * @author 정시원
     */
    @PutMapping("/give-up/{errandIdx}")
    public CommonResult giveUpErrand(@PathVariable("errandIdx") Long errandIdx) throws FirebaseMessagingException {
        errandService.giveUpErrand(errandIdx);
        return responseService.getSuccessResult();
    }

}
