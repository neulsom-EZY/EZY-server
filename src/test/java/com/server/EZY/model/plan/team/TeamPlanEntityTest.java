package com.server.EZY.model.plan.team;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.repository.plan.TeamPlanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TeamPlanEntityTest {

    @Autowired TeamPlanRepository teamPlanRepo;

}