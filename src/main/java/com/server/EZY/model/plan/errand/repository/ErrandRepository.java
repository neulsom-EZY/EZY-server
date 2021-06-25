package com.server.EZY.model.plan.errand.repository;

import com.server.EZY.model.plan.errand.ErrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrandRepository extends JpaRepository<ErrandEntity, Long> {
}
