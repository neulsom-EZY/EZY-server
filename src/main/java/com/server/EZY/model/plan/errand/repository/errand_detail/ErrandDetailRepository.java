package com.server.EZY.model.plan.errand.repository.errand_detail;

import com.server.EZY.model.plan.errand.ErrandDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrandDetailRepository extends JpaRepository<ErrandDetailEntity, Long> {
}
