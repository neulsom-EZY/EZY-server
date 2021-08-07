package com.server.EZY.model.plan.tag.controller;

import com.server.EZY.model.plan.tag.dto.TagSetDto;
import com.server.EZY.model.plan.tag.service.TagService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final ResponseService responseService;

    /**
     * 태그를 추가하는 Controller
     * @param tagSetDto
     * @return getSuccessResult
     * @author 전지환
     */
    @PostMapping("")
    public CommonResult addTag(@RequestBody TagSetDto tagSetDto) {
        tagService.saveTag(tagSetDto);
        return responseService.getSuccessResult();
    }

    /**
     * 태그를 조회하는 Controller
     * @param tagIdx
     * @return
     * @author 배태현
     */
    @GetMapping("/{tagIdx}")
    public CommonResult getTag(@PathVariable("tagIdx") Long tagIdx) {
        return responseService.getSuccessResult();
    }

    /**
     * 태그를 삭제하는 Controller
     * @param tagIdx
     * @return
     * @author 배태현
     */
    @DeleteMapping("/{tagIdx}")
    public CommonResult deleteTag(@PathVariable("tagIdx") Long tagIdx) {
        return responseService.getSuccessResult();
    }
}