package com.server.EZY.model.plan.personal.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import static com.server.EZY.model.plan.personal.QPersonalPlanEntity.personalPlanEntity;

import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PersonalPlanRepositoryCustomImpl implements PersonalPlanRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PersonalPlanEntity> findAllPersonalPlanByMemberEntity(MemberEntity memberEntity) {
        return jpaQueryFactory
                .selectFrom(personalPlanEntity)
                .where(personalPlanEntity.memberEntity.eq(memberEntity)).fetch();
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

    @Override
    public PersonalPlanDto.PersonalPlanDetails getPersonalPlanDetailsByPersonalPlanIdx(MemberEntity memberEntity, Long personalPlanIdx){
        return jpaQueryFactory
                .select(Projections.fields(PersonalPlanDto.PersonalPlanDetails.class,
                        personalPlanEntity.planInfo,
                        personalPlanEntity.period,
                        personalPlanEntity.tagEntity,
                        personalPlanEntity.repetition
                ))
                .from(personalPlanEntity)
                .where(personalPlanEntity.planIdx.eq(personalPlanIdx))
                .fetchOne();
    }
}
