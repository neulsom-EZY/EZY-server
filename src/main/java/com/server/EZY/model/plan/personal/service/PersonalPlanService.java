package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;

import java.util.List;

public interface PersonalPlanService {
    PersonalPlanEntity createPersonalPlan(PersonalPlanSetDto personalPlanSetDto);
    List<PersonalPlanEntity> getAllPersonalPlan();
    PersonalPlanEntity getThisPersonalPlan(Long planIdx);
}
