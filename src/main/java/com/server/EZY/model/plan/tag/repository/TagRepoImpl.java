package com.server.EZY.model.plan.tag.repository;

import static com.server.EZY.model.plan.tag.QTagEntity.tagEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.dto.TagSetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TagRepoImpl implements TagRepoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<TagSetDto> findTagEntitiesByMemberEntity(MemberEntity memberEntity) {
        return queryFactory
                .select(Projections.fields(TagSetDto.class,
                        tagEntity.tag,
                        tagEntity.color
                ))
                .from(tagEntity)
                .where(tagEntity.memberEntity.eq(memberEntity))
                .fetch();
    }
}
