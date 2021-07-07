package com.server.EZY.model.plan.team.repository;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.user.UserEntity;

import java.util.List;

public interface TeamPlanRepositoryCustom {
    // UserEntity 를 param으로 넘겨주어 내 모든 TeamPlan을 조회하는 method
    List<HeadOfPlanEntity> findAllTeamPlanByUserEntity(UserEntity userEntity);
    // UserEntity와 PlanIdx를 param으로 넘겨주어 해당 TeamPlan을 조회하는 method
    HeadOfPlanEntity findThisTeamPlanByUserEntityAndHeadOfPlanIdx(UserEntity userEntity, Long planIdx);
}
