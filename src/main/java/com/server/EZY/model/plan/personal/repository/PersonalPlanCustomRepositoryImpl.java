package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.QMemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.QPersonalPlanDto_PersonalPlanDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.server.EZY.model.member.QMemberEntity.*;
import static com.server.EZY.model.plan.QPlanEntity.planEntity;
import static com.server.EZY.model.plan.personal.QPersonalPlanEntity.personalPlanEntity;
import static com.server.EZY.model.plan.tag.QTagEntity.tagEntity;

@Slf4j
@RequiredArgsConstructor
public class PersonalPlanCustomRepositoryImpl implements PersonalPlanCustomRepository {

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
    @Transactional(readOnly = true)
    public PersonalPlanDto.PersonalPlanDetails getPersonalPlanDetailsByPlanIdx(MemberEntity memberEntity, Long planIdx){
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
                .join(personalPlanEntity.tagEntity, tagEntity)
                .where(
                        personalPlanEntity.memberEntity.eq(memberEntity),
                        personalPlanEntity.planIdx.eq(planIdx)
                ).fetchOne();
    }
}
