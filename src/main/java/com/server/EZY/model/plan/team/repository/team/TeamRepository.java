package com.server.EZY.model.plan.team.repository.team;

import com.server.EZY.model.plan.team.TeamEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    List<TeamEntity> findAllByTeamPlanEntity(TeamPlanEntity teamPlanEntity);
}
