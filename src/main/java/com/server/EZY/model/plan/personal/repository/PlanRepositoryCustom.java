package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.user.UserEntity;

import java.util.List;

public interface PlanRepositoryCustom {
    List<PlanEntity> findAllPersonalPlanByUserEntity (UserEntity userEntity);
}
