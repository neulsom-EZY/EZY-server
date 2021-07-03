package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.plan.planManagement.PlanManagementEntity;
import com.server.EZY.model.plan.planManagement.enumType.PlanDType;
import com.server.EZY.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import static com.server.EZY.model.plan.planManagement.QPlanManagementEntity.planManagementEntity;

import java.util.List;

@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlanManagementEntity> findAllPersonalPlanByUserEntity (UserEntity userEntity) {
        return queryFactory
                .selectFrom(planManagementEntity)
                .where(
                        planManagementEntity.userEntity.eq(userEntity),
                        planManagementEntity.planDType.eq(PlanDType.PERSONAL_PLAN)
                ).fetch();
    }

    @Override
    public PlanManagementEntity findThisPlanByUserEntityAndPlanIdx(UserEntity userEntity, Long planId) {
        return queryFactory
                .selectFrom(planManagementEntity)
                .where(
                        planManagementEntity.userEntity.eq(userEntity),
                        planManagementEntity.personalPlanEntity.personalPlanIdx.eq(planId),
                        planManagementEntity.planDType.eq(PlanDType.PERSONAL_PLAN)
                ).fetchOne();
    }

    @Override
    public PlanManagementEntity findPlanEntityByUserEntity_UserIdxAndPersonalPlanEntity_PersonalPlanIdx(Long userIdx, Long personalPlanIdx) {
        return queryFactory
                .selectFrom(planManagementEntity)
                .where(
                        planManagementEntity.userEntity.userIdx.eq(userIdx),
                        planManagementEntity.personalPlanEntity.personalPlanIdx.eq(personalPlanIdx)
                ).fetchOne();
    }
}
