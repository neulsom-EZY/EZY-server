package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import static com.server.EZY.model.plan.personal.QPersonalPlanEntity.personalPlanEntity;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PersonalPlanRepoImpl implements PersonalPlanRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PersonalPlanEntity> findAllPersonalPlanByMemberEntity(MemberEntity memberEntity) {
        return jpaQueryFactory.selectFrom(personalPlanEntity)
                .where(
                        personalPlanEntity.memberEntity.eq(memberEntity)
                ).fetch();
    }
}
