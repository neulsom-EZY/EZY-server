package com.server.EZY.model.plan.tag.repository;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.dto.TagGetDto;

import java.util.List;

public interface TagRepoCustom {
    List<TagGetDto> getTagInfo(MemberEntity memberEntity);
}
