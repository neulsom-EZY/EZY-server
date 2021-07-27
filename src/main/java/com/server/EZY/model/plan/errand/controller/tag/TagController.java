package com.server.EZY.model.plan.errand.controller.tag;

import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final ResponseService responseService;

    @PostMapping("")
    public CommonResult addTag(String tag) {
        return responseService.getSuccessResult();
    }

    @GetMapping("/{tagIdx}")
    public CommonResult selectTag(@PathVariable("tagIdx") Long tagIdx) {
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/{tagIdx}")
    public CommonResult deleteTag(@PathVariable("tagIdx") Long tagIdx) {
        return responseService.getSuccessResult();
    }
}
