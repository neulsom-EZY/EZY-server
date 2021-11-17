package com.server.EZY.model.plan.tag.controller;

import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagGetDto;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import com.server.EZY.model.plan.tag.service.TagService;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
     * @author 전지환, 배태현
     */
    @PostMapping("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult addTag(@RequestBody TagSetDto tagSetDto) {
        TagEntity savedTagEntity = tagService.saveTag(tagSetDto);
        return responseService.getSingleResult(new TagGetDto.TagIdx(savedTagEntity.getTagIdx()));
    }

    /**
     * 태그를 조회하는 Controller
     * @return getListResult
     * @author 전지환, 배태현
     */
    @GetMapping("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult getAllTag() {
        return responseService.getListResult(tagService.getAllTag());
    }

    /**
     * 태그를 삭제하는 Controller
     * @param tagIdx
     * @return getSuccessResult
     * @author 전지환, 배태현
     */
    @DeleteMapping("/{tagIdx}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "RefreshToken", value = "로그인 성공 후 refresh_token", required = false, dataType = "String", paramType = "header")
    })
    public CommonResult deleteThisTag(@PathVariable("tagIdx") Long tagIdx) throws Exception {
        tagService.deleteTag(tagIdx);
        return responseService.getSuccessResult();
    }
}