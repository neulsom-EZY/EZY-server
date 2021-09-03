package com.server.EZY.model.plan.tag.repository;

import static com.server.EZY.model.plan.tag.QTagEntity.tagEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.dto.TagGetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Repository
public class TagRepoImpl implements TagRepoCustom  {
    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<TagGetDto> findMyTagEntitiesByMemberEntity(MemberEntity memberEntity) {
        log.warn("========== query method에 도달하였습니다. ==========");
        List<TagGetDto> tagGetDtos = queryFactory
                .select(Projections.fields(TagGetDto.class,
                        tagEntity.tag.as("tag"),
                        tagEntity.color
                ))
                // 기본 생성자를 private으로 설정하고 select하고 싶을 때
//                .select(Projections.fields(TagSetDto.class,
//                        tagEntity.tag,
//                        tagEntity.color
//                ))
                .from(tagEntity)
                .where(tagEntity.memberEntity.eq(memberEntity))
                .fetch();

        log.info("tagGetDtos는.. {}", tagGetDtos);
        return tagGetDtos;
    }
}
