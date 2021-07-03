package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.plan.planManagement.PlanManagementEntity;
import com.server.EZY.model.user.UserEntity;

import java.util.List;

public interface PlanRepositoryCustom {
    List<PlanManagementEntity> findAllPersonalPlanByUserEntity (UserEntity userEntity);
    PlanManagementEntity findThisPlanByUserEntityAndPlanIdx (UserEntity userEntity, Long personalPlanId);
    PlanManagementEntity findPlanEntityByUserEntity_UserIdxAndPersonalPlanEntity_PersonalPlanIdx (Long userIdx, Long personalPlanIdx);
}
