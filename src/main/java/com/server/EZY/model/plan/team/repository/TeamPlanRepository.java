package com.server.EZY.model.plan.team.repository;


import com.server.EZY.model.plan.team.TeamPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamPlanRepository extends JpaRepository<TeamPlanEntity, Long> {}

