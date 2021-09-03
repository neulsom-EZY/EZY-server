package com.server.EZY.model.plan.tag.repository;

import static com.server.EZY.model.plan.tag.QTagEntity.tagEntity;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.dto.TagGetDto;
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
    public List<TagGetDto> findTagEntitiesByMemberEntity(MemberEntity memberEntity) {
        log.info("findTagEntities에 진입하였습니다.");
        List<TagGetDto> tagGetDtos = queryFactory
                .select(Projections.fields(TagGetDto.class,
                        tagEntity.tag,
                        tagEntity.color.red,
                        tagEntity.color.green,
                        tagEntity.color.blue
                ))
                .from(tagEntity)
                .where(tagEntity.memberEntity.eq(memberEntity))
                .groupBy(tagEntity.tag, tagEntity.color)
                .fetch();

        log.info("tagGetDtos는.. {}", tagGetDtos);
        return tagGetDtos;
    }
}
