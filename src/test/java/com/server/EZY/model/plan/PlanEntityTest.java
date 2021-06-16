package com.server.EZY.model.plan;

import com.server.EZY.repository.plan.PersonalPlanRepository;
import com.server.EZY.repository.plan.PlanRepository;
import com.server.EZY.repository.plan.TeamPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlanEntityTest {

    @Autowired PlanRepository PlanRepo;
    @Autowired PersonalPlanRepository personalPlanRepo;
    @Autowired TeamPlanRepository teamPlanRepo;


}