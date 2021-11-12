package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;

import java.time.LocalDate;
import java.util.List;

public interface PersonalPlanService {
    PersonalPlanEntity createPersonalPlan(PersonalPlanDto.PersonalPlanSet personalPlanSetDto);
    List<PersonalPlanDto.PersonalPlanListDto> getAllPersonalPlan();
    List<PersonalPlanEntity> getThisDatePersonalPlanEntities(LocalDate startDate);
    List<PersonalPlanEntity> getPersonalPlanEntitiesBetween(LocalDate startDate, LocalDate endDate);
    PersonalPlanDto.PersonalPlanDetails getThisPersonalPlan(Long planIdx);
    void deleteThisPersonalPlan(Long planIdx);
    PersonalPlanEntity updateThisPersonalPlan(Long planIdx, PersonalPlanDto.PersonalPlanSet personalPlanSetDto) throws Exception;
}
