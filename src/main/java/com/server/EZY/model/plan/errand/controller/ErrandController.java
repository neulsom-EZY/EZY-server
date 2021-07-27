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

    /**
     * 심부름 알람을 보내는 Controller (사용자 입장에선 알람을 받게된다)
     * @param username
     * @return
     * @author 배태현
     */
    @GetMapping("/{username}")
    public CommonResult getErrandAlarm(@PathVariable("username") String username) {
        return responseService.getSuccessResult();
    }

    /**
     * 해당 심부름을 상세조회하는 Controller
     * @param errandIdx
     * @return
     * @author 배태현
     */
    @GetMapping("/errandIdx")
    public CommonResult thisErrandDetailSelect(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }

    /**
     * 심부름을 보내는 Controller
     * @return
     * @author 배태현
     */
    @PostMapping("/send")
    public CommonResult sendErrand() {
        return responseService.getSuccessResult();
    }

    /**
     * 심부름을 받은자가 삭제하는 Controller
     * @param errandIdx
     * @return
     * @author 배태현
     */
    @DeleteMapping("/recipient/{errandIdx}")
    public CommonResult deleteByReceiver(@PathVariable("errandIdx") Long errandIdx) {
        return responseService.getSuccessResult();
    }
}
