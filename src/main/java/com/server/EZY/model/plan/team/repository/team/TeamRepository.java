package com.server.EZY.model.plan.team.repository.team;

import com.server.EZY.model.plan.team.TeamPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamPlanEntity, Long> {
}
