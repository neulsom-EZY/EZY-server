package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PersonalPlanService {
    PersonalPlanEntity createPersonalPlan(PersonalPlanSetDto personalPlanSetDto);
    List<PersonalPlanEntity> getAllPersonalPlan();
    List<PersonalPlanEntity> getThisDatePersonalEntities(LocalDate startDate);
    PersonalPlanEntity getThisPersonalPlan(Long planIdx);
    void deleteThisPersonalPlan(Long planIdx);
    PersonalPlanEntity updateThisPersonalPlan(Long planIdx, PersonalPlanSetDto personalPlanSetDto) throws Exception;
}
