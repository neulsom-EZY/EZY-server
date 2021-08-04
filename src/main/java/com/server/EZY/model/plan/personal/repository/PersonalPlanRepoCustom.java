package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PersonalPlanRepoCustom {
    List<PersonalPlanEntity> findAllPersonalPlanByMemberEntity(MemberEntity memberEntity);
    List<PersonalPlanEntity> findAllPersonalPlanEntitiesByMemberEntityAndPeriodStartDateTime(MemberEntity memberEntity, LocalDate startDate);
    PersonalPlanEntity findThisPersonalPlanByMemberEntityAndPlanIdx(MemberEntity memberEntity, Long planIdx);
}
