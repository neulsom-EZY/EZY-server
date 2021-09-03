package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalPlanRepository extends JpaRepository<PersonalPlanEntity, Long>, PersonalPlanRepositoryCustom {
}
