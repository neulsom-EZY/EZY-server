package com.server.EZY.model.plan.errand.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
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
     * 심부름을 받은이가 심부름을 확인(읽음)여부 Controller
     * @param errandIdx
     * @return
     * @author 배태현
     */
    @GetMapping("/check/{errandIdx}")
    public CommonResult readThisSchedule(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    /**
     * 심부름 수락 Controller
     * @param errandIdx
     * @return
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
     * @param errandIdx
     * @return
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
     * @return
     * @author 배태현
     */
    @PutMapping("/complete/{errandIdx}")
    public CommonResult completeErrand(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    /**
     * 심부름 실패 Controller
     * @param errandIdx
     * @return
     * @author 배태현
     */
    @PutMapping("/fail/{errandIdx}")
    public CommonResult failErrand(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }
}
