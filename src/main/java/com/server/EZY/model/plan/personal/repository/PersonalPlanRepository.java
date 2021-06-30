package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalPlanRepository extends JpaRepository<PersonalPlanEntity, Long> {
    // param 으로 받은 personalPlanIdx를 통해 PersonalPlanEntity를 찾습니다.
    PersonalPlanEntity findByPersonalPlanIdx(Long personalPlanIdx);
    // testCode 작성시 유용하게 사용하기 위해, PersonalPlanName으로 PersonalPlanEntity를 찾습니다.
    PersonalPlanEntity findByPlanName(String planName);
}
