package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.user.UserEntity;

import java.util.List;

public interface PlanRepositoryCustom {
    List<HeadOfPlanEntity> findAllPersonalPlanByUserEntity (UserEntity userEntity);
    HeadOfPlanEntity findThisPlanByUserEntityAndPlanIdx (UserEntity userEntity, Long personalPlanId);
    HeadOfPlanEntity findPlanEntityByUserEntity_UserIdxAndPersonalPlanEntity_PersonalPlanIdx (Long userIdx, Long personalPlanIdx);
}
