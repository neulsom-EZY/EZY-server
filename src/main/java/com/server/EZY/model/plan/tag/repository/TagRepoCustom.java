package com.server.EZY.model.plan.tag.repository;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;

import java.util.List;

public interface TagRepoCustom {
    List<TagSetDto> findTagEntitiesByMemberEntity(MemberEntity memberEntity);
}
