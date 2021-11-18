package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;

import java.time.LocalDate;
import java.util.List;

public interface PersonalPlanService {
    PersonalPlanEntity createPersonalPlan(PersonalPlanDto.PersonalPlanSet personalPlanSetDto);
    List<PersonalPlanDto.PersonalPlanListDto> getAllPersonalPlan();
    List<PersonalPlanDto.PersonalPlanListDto> getThisDatePersonalPlanEntities(LocalDate startDate);
    List<PersonalPlanDto.PersonalPlanListDto> getPersonalPlanEntitiesBetween(LocalDate startDate, LocalDate endDate);
    PersonalPlanDto.PersonalPlanDetails getThisPersonalPlan(Long planIdx);
    void deleteThisPersonalPlan(Long planIdx);
    PersonalPlanEntity updateThisPersonalPlan(Long planIdx, PersonalPlanDto.PersonalPlanSet personalPlanSetDto) throws Exception;
}
