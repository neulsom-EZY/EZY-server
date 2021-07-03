package com.server.EZY.model.plan.team.service;

import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.plan.repository.PlanRepository;
import com.server.EZY.model.plan.team.dto.TeamPlanDto;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamPlanService {
    private final PlanRepository planRepository;
    private final CurrentUserUtil currentUserUtil;

    @Transactional
    public PlanEntity saveTeamPlan(TeamPlanDto teamPlanDto){
        UserEntity getCurrentUser = currentUserUtil.getCurrentUser();
        return null;
    }
}
