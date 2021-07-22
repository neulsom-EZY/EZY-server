package com.server.EZY.model.plan.headOfPlan.repository;

import com.server.EZY.model.plan.personal.repository.PersonalPlanRepositoryCustom;
import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.team.repository.TeamPlanRepositoryCustom;
import com.server.EZY.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeadOfPlanRepository extends JpaRepository<HeadOfPlanEntity, Long>, PersonalPlanRepositoryCustom, TeamPlanRepositoryCustom, HOPRepositoryCustom {
    List<HeadOfPlanEntity> findAllPersonalPlanByUserEntityAndPersonalPlanEntityNotNull(UserEntity userEntity);
    List<HeadOfPlanEntity> findAllTeamPlanByUserEntityAndTeamPlanEntityNotNull(UserEntity userEntity);
}
