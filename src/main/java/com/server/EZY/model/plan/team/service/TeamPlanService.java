package com.server.EZY.model.plan.team.service;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.team.dto.TeamPlanDto;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamPlanService {
    private final CurrentUserUtil currentUserUtil;

    @Transactional
    public HeadOfPlanEntity saveTeamPlan(TeamPlanDto teamPlanDto){
        UserEntity getCurrentUser = currentUserUtil.getCurrentUser();
        return null;
    }
}
