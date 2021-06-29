package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.plan.enumType.PlanDType;
import com.server.EZY.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import static com.server.EZY.model.plan.plan.QPlanEntity.planEntity;

import java.util.List;

@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlanEntity> findAllPersonalPlanByUserEntity (UserEntity userEntity) {
        return queryFactory
                .selectFrom(planEntity)
                .where(
                        planEntity.userEntity.eq(userEntity),
                        planEntity.planDType.eq(PlanDType.PERSONAL_PLAN)
                ).fetch();
    }

    @Override
    public PlanEntity findByPersonalPlanByUserEntityAndPlanIdx(UserEntity userEntity, Long planId) {
        return queryFactory
                .selectFrom(planEntity)
                .where(
                        planEntity.userEntity.eq(userEntity),
                        planEntity.personalPlanEntity.personalPlanIdx.eq(planId),
                        planEntity.planDType.eq(PlanDType.PERSONAL_PLAN)
                ).fetchOne();
    }
}
