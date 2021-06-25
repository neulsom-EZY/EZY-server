package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
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
    @Autowired
    private PersonalPlanRepository personalPlanRepository;

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

    @Test
    public void 개인일정변경() throws Exception {
        /**
         * Given
         * wannaChangeIdx: 변경을 원하는 일정의 Idx 를 저장합니다.
         * wannaUpdatePlan: 변경을 원하는 내용을 저장합니다.
         */
        Long wannaChangeIdx = personalPlanRepository.findByPlanName("지환이랑 놀기").getPersonalPlanIdx();
        PersonalPlanUpdateDto wannaUpdatePlan = PersonalPlanUpdateDto.builder()
                .planName("태현이랑 놀기")
                .when(Calendar.getInstance())
                .where("잠실, 강남")
                .who("유진이랑")
                .repeat(false)
                .build();

        // when
        personalPlanService.updatePersonalPlan(wannaUpdatePlan, wannaChangeIdx);

        // then
        assertEquals(true, personalPlanRepository.findByPlanName("태현이랑 놀기") != null);
    }
}