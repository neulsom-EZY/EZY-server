package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.plan.enumType.PlanDType;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.server.EZY.model.plan.plan.QPlanEntity.planEntity;

public class PlanRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    // Bean 등록된 JPAQueryFactory 를 생성자 인젝션으로 주입 받아 사용합니다.
    public PlanRepositorySupport(JPAQueryFactory queryFactory) {
        super(PlanEntity.class);
        this.queryFactory = queryFactory;
    }

    public List<PlanEntity> findAllByUserId(Long userId){
        return queryFactory
                .selectFrom(planEntity)
                .where(
                        planEntity.planDType.eq(PlanDType.valueOf("PERSONAL_PLAN")),
                        planEntity.userEntity.userIdx.eq(userId)
                )
                .fetch();
    }
}
