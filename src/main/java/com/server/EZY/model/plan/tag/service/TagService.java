package com.server.EZY.model.plan.tag.service;

import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;

public interface TagService {
    TagEntity saveTag(TagSetDto tagSetDto);
    void deleteTag(Long tagIdx) throws Exception;
}
