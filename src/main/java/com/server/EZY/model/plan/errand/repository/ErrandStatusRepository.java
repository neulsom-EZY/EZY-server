package com.server.EZY.model.plan.errand.repository;

import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrandStatusRepository extends JpaRepository<ErrandStatusEntity, Long> {
}
