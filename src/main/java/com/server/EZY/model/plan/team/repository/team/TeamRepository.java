package com.server.EZY.model.plan.team.repository.team;

import com.server.EZY.model.plan.team.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
}
