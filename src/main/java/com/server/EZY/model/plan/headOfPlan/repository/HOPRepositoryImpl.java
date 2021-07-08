package com.server.EZY.model.plan.headOfPlan.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import static com.server.EZY.model.plan.headOfPlan.QHeadOfPlanEntity.headOfPlanEntity;

import java.util.List;

@RequiredArgsConstructor
public class HOPRepositoryImpl implements HOPRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<HeadOfPlanEntity> findAllHeadOfPlanByUserEntity(UserEntity userEntity) {
        return queryFactory
                .selectFrom(headOfPlanEntity)
                .where(
                        headOfPlanEntity.userEntity.eq(userEntity)
                ).fetch();
    }
}
