package com.server.EZY.model.plan.team.repository;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.user.UserEntity;

import java.util.List;

public interface TeamPlanRepositoryCustom {
    List<HeadOfPlanEntity> findAllTeamPlanByUserEntity(UserEntity userEntity);
}
