package com.server.EZY.repository.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalPlanRepository extends JpaRepository<PersonalPlanRepository, Long> {
}
