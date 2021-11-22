package com.server.EZY.model.plan.errand.controller;

import com.server.EZY.model.plan.errand.dto.ErrandResponseDto;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.service.ErrandService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/errand")
@RequiredArgsConstructor
public class ErrandController {

    private final ErrandService errandService;
    private final ResponseService responseService;

    /**
     * 모든 심부름를 조회하는 controller
     *
     * @author 전지환
     * @return getListResult
     */
    @GetMapping("/")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult getAllMyErrands(){
        List<ErrandResponseDto.ErrandPreview> allMyErrands = errandService.findAllMyErrands();
        return responseService.getListResult(allMyErrands);
    }

    /**
     * 해당 심부름을 상세조회하는 Controller
     *
     * @param errandIdx
     * @return getSingleResult
     * @author 전지환, 배태현
     */
    @GetMapping("/{errandIdx}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult getThisErrandDetails(@PathVariable(value = "errandIdx") Long errandIdx) throws Exception {
        return responseService.getSingleResult(errandService.findErrandDetails(errandIdx));
    }

    /**
     * 심부름 보내기
     * @return getSuccessResult 전송
     * @author 배태현, 전지환
     */
    @PostMapping("/send")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult sendErrand(@RequestBody ErrandSetDto errandSetDto) throws Exception {
        errandService.sendErrand(errandSetDto);
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
