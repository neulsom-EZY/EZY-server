package com.server.EZY.model.plan.tag.repository;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepoCustom {
    List<TagSetDto> findTagEntitiesByMemberEntity(MemberEntity memberEntity);
}
