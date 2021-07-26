package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;

public interface PersonalPlanService {
    public PersonalPlanEntity createPersonalPlan(PersonalPlanSetDto personalPlanSetDto);
}
