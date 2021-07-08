package com.server.EZY.model.plan.headOfPlan.repository;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.user.UserEntity;

import java.util.List;

public interface HOPRepositoryCustom {
    List<HeadOfPlanEntity> findAllHeadOfPlanByUserEntity(UserEntity userEntity);
}
