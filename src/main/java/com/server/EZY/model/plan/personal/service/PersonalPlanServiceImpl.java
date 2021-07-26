package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalPlanServiceImpl implements PersonalPlanService{

    private final CurrentUserUtil currentUserUtil;

    @Override
    public PersonalPlanEntity createPersonalPlan(PersonalPlanSetDto personalPlan) {
        return null;
    }
}
