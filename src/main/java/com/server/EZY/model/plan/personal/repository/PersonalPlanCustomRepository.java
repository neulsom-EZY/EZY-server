package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PersonalPlanCustomRepository {
    List<PersonalPlanDto.PersonalPlanListDto> findAllPersonalPlanByMemberEntity(MemberEntity memberEntity);
    List<PersonalPlanEntity> findPersonalPlanEntitiesByMemberEntityAndPeriod_StartDateTimeBetween(MemberEntity memberEntity, LocalDateTime startDateTime, LocalDateTime endDateTime);
    PersonalPlanEntity findThisPersonalPlanByMemberEntityAndPlanIdx(MemberEntity memberEntity, Long planIdx);
    PersonalPlanDto.PersonalPlanDetails findPersonalPlanDetailsByPlanIdx(MemberEntity memberEntity, Long planIdx);
}
