package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PersonalPlanRepoCustom {
    List<PersonalPlanEntity> findAllPersonalPlanByMemberEntity(MemberEntity memberEntity);
    List<PersonalPlanEntity> findPersonalPlanEntitiesByMemberEntityAndPeriod_StartDateTimeBetween(MemberEntity memberEntity, LocalDateTime startDateTime, LocalDateTime endDateTime);
    PersonalPlanEntity findThisPersonalPlanByMemberEntityAndPlanIdx(MemberEntity memberEntity, Long planIdx);
}
