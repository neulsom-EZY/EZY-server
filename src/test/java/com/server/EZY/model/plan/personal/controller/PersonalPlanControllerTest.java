package com.server.EZY.model.plan.personal.controller;

import com.server.EZY.testConfig.AbstractControllerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonalPlanControllerTest extends AbstractControllerTest {
    @Autowired
    private PersonalPlanController personalPlanController;

    @Override
    protected Object controller() {
        return personalPlanController;
    }
}