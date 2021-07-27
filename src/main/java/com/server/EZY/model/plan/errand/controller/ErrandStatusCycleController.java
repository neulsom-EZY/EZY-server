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
     * @author 배태현
     */
    @PutMapping("/accept/{errandIdx}")
    public CommonResult acceptErrand(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    /**
     * 심부름 거절 Controller
     * @param errandIdx
     * @return
     * @author 배태현
     */
    @PutMapping("/reject/{errandIdx}")
    public CommonResult rejectErrand(@PathVariable("errandIdx") Long errandIdx) {
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
