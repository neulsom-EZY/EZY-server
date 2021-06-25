package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class PersonalPlanServiceTest {

    @Autowired
    private PersonalPlanService personalPlanService;

    @BeforeEach
    public void 개인일정세트(){
        PersonalPlanDto myPersonalPlan = PersonalPlanDto.builder()
                .planName("지환이랑 놀기")
                .when(Calendar.getInstance())
                .where("롯데월드")
                .what("리프레시 타임")
                .who("시원이, 태현이")
                .repeat(false)
                .build();

        String result = personalPlanService.savePersonalPlan(myPersonalPlan);
    }

    @Test
    public void 개인일정추가(){
        // Given
        PersonalPlanDto myPersonalPlan = PersonalPlanDto.builder()
                .planName("지환이랑 놀기")
                .when(Calendar.getInstance())
                .where("롯데월드")
                .what("리프레시 타임")
                .who("시원이, 태현이")
                .repeat(false)
                .build();

        //When
        String result = personalPlanService.savePersonalPlan(myPersonalPlan);

        //Then
        assertEquals("지환이랑 놀기의 제목의 일정이 추가 되었습니다.", result);
        System.out.println("============== result = "+ result + " ===================");
    }
}