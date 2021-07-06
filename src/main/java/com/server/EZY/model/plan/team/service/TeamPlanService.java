package com.server.EZY.model.plan.team.service;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.headOfPlan.repository.HeadOfPlanRepository;
import com.server.EZY.model.plan.team.dto.TeamPlanDto;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamPlanService {
    private final HeadOfPlanRepository headOfPlanRepository;
    private final CurrentUserUtil currentUserUtil;

    @Transactional
    public HeadOfPlanEntity saveTeamPlan(TeamPlanDto teamPlanDto){
        // 현재 로그인 된 UserEntity 할당.
        UserEntity getCurrentUser = currentUserUtil.getCurrentUser();
        // team 일정과 user를 세트.
        HeadOfPlanEntity headOfPlanEntity = new HeadOfPlanEntity(
                teamPlanDto.toEntity(),
                getCurrentUser
        );
        return headOfPlanRepository.save(headOfPlanEntity);
    }

    public HeadOfPlanEntity getAllMyTeamPlan(){
        UserEntity getCurrentUser = currentUserUtil.getCurrentUser();

        return null;
    }
}
