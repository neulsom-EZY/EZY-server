package com.server.EZY.model.plan.team.service;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.headOfPlan.repository.HeadOfPlanRepository;
import com.server.EZY.model.plan.team.dto.TeamPlanDto;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamPlanService {
    private final HeadOfPlanRepository headOfPlanRepository;
    private final CurrentUserUtil currentUserUtil;

    /**
     * 이 메서드는 팀 일정 내용을 넘겨주면 team 일정을 저장해주는 로직입니다.
     * @param teamPlanDto
     * @return HeadOfPlanEntity
     * @author 전지환
     */
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

    /**
     * 이 메서드는 유저 엔티티를 넘겨주면 그 유저에 해당하는 모든 팀 일정을 조회합니다.
     * @return HeadOfPlanEntity
     * @author 전지환
     */
    public List<HeadOfPlanEntity> getAllMyTeamPlan(){
        UserEntity getCurrentUser = currentUserUtil.getCurrentUser();
        return headOfPlanRepository.findAllTeamPlanByUserEntity(getCurrentUser);
    }
}
