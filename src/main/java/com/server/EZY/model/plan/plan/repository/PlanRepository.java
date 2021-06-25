package com.server.EZY.model.plan.plan.repository;

import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    List<PlanEntity> findAllPersonalPlanByUserEntityAndPersonalPlanEntityNotNull(UserEntity userEntity);
    List<PlanEntity> findAllTeamPlanByUserEntityAndTeamPlanEntityNotNull(UserEntity userEntity);
}
