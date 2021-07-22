package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.personal.enumType.PlanDType;
import com.server.EZY.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import static com.server.EZY.model.plan.headOfPlan.QHeadOfPlanEntity.headOfPlanEntity;

import java.util.List;

@RequiredArgsConstructor
public class PersonalPlanRepositoryImpl implements PersonalPlanRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HeadOfPlanEntity> findAllPersonalPlanByUserEntity (UserEntity userEntity) {
        return queryFactory
                .selectFrom(headOfPlanEntity)
                .where(
                        headOfPlanEntity.userEntity.eq(userEntity),
                        headOfPlanEntity.planDType.eq(PlanDType.PERSONAL_PLAN)
                ).fetch();
    }

    @Override
    public HeadOfPlanEntity findThisPlanByUserEntityAndHeadOfPlanIdx(UserEntity userEntity, Long planId) {
        return queryFactory
                .selectFrom(headOfPlanEntity)
                .where(
                        headOfPlanEntity.userEntity.eq(userEntity),
                        headOfPlanEntity.personalPlanEntity.personalPlanIdx.eq(planId),
                        headOfPlanEntity.planDType.eq(PlanDType.PERSONAL_PLAN)
                ).fetchOne();
    }

    @Override
    public HeadOfPlanEntity findPlanEntityByUserEntity_UserIdxAndPersonalPlanEntity_PersonalPlanIdx(Long userIdx, Long personalPlanIdx) {
        return queryFactory
                .selectFrom(headOfPlanEntity)
                .where(
                        headOfPlanEntity.userEntity.userIdx.eq(userIdx),
                        headOfPlanEntity.personalPlanEntity.personalPlanIdx.eq(personalPlanIdx)
                ).fetchOne();
    }
}
