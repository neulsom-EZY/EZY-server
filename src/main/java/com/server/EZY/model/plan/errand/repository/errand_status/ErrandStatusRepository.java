package com.server.EZY.model.plan.errand.repository.errand_status;

import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrandStatusRepository extends JpaRepository<ErrandStatusEntity, Long> {
}
