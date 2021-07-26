package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;

import java.util.List;

public interface PersonalPlanService {
    public PersonalPlanEntity createPersonalPlan(PersonalPlanSetDto personalPlanSetDto);
    public List<PersonalPlanEntity> getAllPersonalPlan();
}
