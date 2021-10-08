package com.server.EZY.model.plan.tag.service;

import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagGetDto;
import com.server.EZY.model.plan.tag.dto.TagSetDto;

import java.util.List;

public interface TagService {
    TagEntity saveTag(TagSetDto tagSetDto);
    List<TagGetDto> getAllTag();
    void deleteTag(Long tagIdx) throws Exception;
}
