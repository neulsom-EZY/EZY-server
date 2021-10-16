package com.server.EZY.model.plan.personal.repository;

import static com.server.EZY.model.plan.personal.QPersonalPlanEntity.personalPlanEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PersonalPlanQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    //TODO 개인일정 전체조회

    //TODO 개인일정 기간별 조회

    //TODO 해당 개인일정 조회
}
