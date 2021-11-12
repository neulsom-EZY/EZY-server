package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.QPersonalPlanDto_PersonalPlanDetails;
import com.server.EZY.model.plan.personal.dto.QPersonalPlanDto_PersonalPlanListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.server.EZY.model.plan.personal.QPersonalPlanEntity.personalPlanEntity;
import static com.server.EZY.model.plan.tag.QTagEntity.tagEntity;

/**
 * 개인일정의 동적쿼리를 담당하는 클래스이다.
 *
 * @version 1
 * @author 전지환
 */
@Slf4j
@RequiredArgsConstructor
public class PersonalPlanCustomRepositoryImpl implements PersonalPlanCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 모든 개인일정을 조회하는 쿼리 메소드.
     *
     * @param memberEntity
     * @return List<PersonalPlanDto.PersonalPlanListDto>
     * @author 전지환
     */
    @Override
    public List<PersonalPlanDto.PersonalPlanListDto> findAllPersonalPlanByMemberEntity(MemberEntity memberEntity) {
        return jpaQueryFactory
                .select(new QPersonalPlanDto_PersonalPlanListDto(
                        personalPlanEntity.planIdx,
                        personalPlanEntity.planInfo,
                        personalPlanEntity.period,
                        personalPlanEntity.tagEntity.tagIdx,
                        personalPlanEntity.tagEntity.tag,
                        personalPlanEntity.tagEntity.color,
                        personalPlanEntity.repetition
                ))
                .from(personalPlanEntity)
                .leftJoin(personalPlanEntity.tagEntity, tagEntity)
                .where(personalPlanEntity.memberEntity.eq(memberEntity))
                .fetch();
    }

    @Override
    public List<PersonalPlanEntity> findPersonalPlanEntitiesByMemberEntityAndPeriod_StartDateTimeBetween(
            MemberEntity memberEntity, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return jpaQueryFactory
                .selectFrom(personalPlanEntity)
                .where(
                        personalPlanEntity.memberEntity.eq(memberEntity),
                        personalPlanEntity.period.startDateTime.between(startDateTime, endDateTime)
                ).fetch();
    }

    @Override
    public PersonalPlanEntity findThisPersonalPlanByMemberEntityAndPlanIdx(MemberEntity memberEntity, Long planIdx) {
        return jpaQueryFactory
                .selectFrom(personalPlanEntity)
                .where(
                        personalPlanEntity.memberEntity.eq(memberEntity),
                        personalPlanEntity.planIdx.eq(planIdx)
                ).fetchOne();
    }

    /**
     * 단건 개일일정을 조회하는 쿼리 메소드.
     *
     * @param memberEntity
     * @param planIdx
     * @return PersonalPlanDto.PersonalPlanDetails - @QueryProjection
     * @author 전지환
     */
    @Override
    @Transactional(readOnly = true)
    public PersonalPlanDto.PersonalPlanDetails findPersonalPlanDetailsByPlanIdx(MemberEntity memberEntity, Long planIdx){
        return jpaQueryFactory
                .select(new QPersonalPlanDto_PersonalPlanDetails(
                        personalPlanEntity.planInfo,
                        personalPlanEntity.period,
                        personalPlanEntity.tagEntity.tagIdx,
                        personalPlanEntity.tagEntity.tag,
                        personalPlanEntity.tagEntity.color,
                        personalPlanEntity.repetition
                ))
                .from(personalPlanEntity)
                .leftJoin(personalPlanEntity.tagEntity, tagEntity)
                .where(
                        personalPlanEntity.memberEntity.eq(memberEntity),
                        personalPlanEntity.planIdx.eq(planIdx)
                ).fetchOne();
    }
}
