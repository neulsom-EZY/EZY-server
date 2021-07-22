package com.server.EZY.model.plan.team.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.personal.enumType.PlanDType;
import com.server.EZY.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import static com.server.EZY.model.plan.headOfPlan.QHeadOfPlanEntity.headOfPlanEntity;

import java.util.List;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamPlanRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HeadOfPlanEntity> findAllTeamPlanByUserEntity(UserEntity userEntity) {
        return queryFactory
                .selectFrom(headOfPlanEntity)
                .where(
                        headOfPlanEntity.userEntity.eq(userEntity),
                        headOfPlanEntity.planDType.eq(PlanDType.TEAM_PLAN)
                ).fetch();
    }

    @Override
    public HeadOfPlanEntity findThisTeamPlanByUserEntityAndHeadOfPlanIdx(UserEntity userEntity, Long planIdx) {
        return queryFactory
                .selectFrom(headOfPlanEntity)
                .where(
                        headOfPlanEntity.userEntity.eq(userEntity),
                        headOfPlanEntity.headOfPlanIdx.eq(planIdx),
                        headOfPlanEntity.planDType.eq(PlanDType.TEAM_PLAN)
                ).fetchOne();
    }
}
