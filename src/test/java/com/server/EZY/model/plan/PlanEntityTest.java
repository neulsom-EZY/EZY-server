package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.user.Permission;
import com.server.EZY.model.user.Role;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.repository.plan.PersonalPlanRepository;
import com.server.EZY.repository.plan.PlanRepository;
import com.server.EZY.repository.plan.TeamPlanRepository;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

@DataJpaTest
class PlanEntityTest {

    @Autowired PlanRepository planRepo;
    @Autowired PersonalPlanRepository personalPlanRepo;
    @Autowired TeamPlanRepository teamPlanRepo;

    PersonalPlanEntity personalPlanEntityInit(){
        return PersonalPlanEntity.builder()
                .planName(RandomString.make(10))
                .when(Calendar.getInstance())
                .where(RandomString.make(20))
                .repeat(false)
                .build();
    }


}