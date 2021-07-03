package com.server.EZY.model.plan.planManagement.repository;

import com.server.EZY.model.plan.personal.repository.PlanRepositoryCustom;
import com.server.EZY.model.plan.planManagement.PlanManagementEntity;
import com.server.EZY.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<PlanManagementEntity, Long>, PlanRepositoryCustom {
    List<PlanManagementEntity> findAllPersonalPlanByUserEntityAndPersonalPlanEntityNotNull(UserEntity userEntity);
    List<PlanManagementEntity> findAllTeamPlanByUserEntityAndTeamPlanEntityNotNull(UserEntity userEntity);
}
