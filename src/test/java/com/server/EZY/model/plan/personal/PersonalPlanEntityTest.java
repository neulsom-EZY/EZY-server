package com.server.EZY.model.plan.personal;

import com.server.EZY.repository.plan.PersonalPlanRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonalPlanEntityTest {

    @Autowired private PersonalPlanRepository personalPlanRepo;

    @Test @DisplayName("PersonalPlanEntity 제약조건의 최대길이에 따라 저장이 잘 되는지 검증")
    void PersonalPlanEntity_최대길이_제약조건_검증(){
        // Given
        String PLAN_NAME = "PersonalPlanEntity_최대길이_제약조건검증";
        Calendar WHEN = Calendar.getInstance();
        WHEN.set(2021, Calendar.JUNE, 12);
        WHEN.add(Calendar.HOUR_OF_DAY, 12);
        WHEN.add(Calendar.MINUTE, 30);
        String WHERE = "광주소프트웨어마이스터고등학교";
        String WHAT = "EZY 개발";
        String WHO = "전지환, 정시원";
        boolean REPEAT = true;

        PersonalPlanEntity personalPlanEntity = PersonalPlanEntity.builder()
                .planName(PLAN_NAME)
                .when(WHEN)
                .where(WHERE)
                .what(WHAT)
                .who(WHO)
                .repeat(REPEAT)
                .build();

        PersonalPlanEntity savePersonalPlanEntity = personalPlanRepo.save(personalPlanEntity);

        // When
        //DB에 저정된 PersonalPlanEntity 가져오기
        PersonalPlanEntity savedDbPersonalPlanEntity = personalPlanRepo.getById(savePersonalPlanEntity.getPersonalPlanIdx());

        // Than
        // DB에 저장된값이 처음 입력한값이랑 같은지 비교
        assertEquals(savedDbPersonalPlanEntity.getPersonalPlanIdx(), savedDbPersonalPlanEntity.getPersonalPlanIdx());
        assertEquals(savedDbPersonalPlanEntity.getPlanName(), PLAN_NAME);
        assertEquals(savedDbPersonalPlanEntity.getWhen(), WHEN);
        assertEquals(savedDbPersonalPlanEntity.getWhere(), WHERE);
        assertEquals(savedDbPersonalPlanEntity.getWhat(), WHAT);
        assertEquals(savedDbPersonalPlanEntity.getWho(), WHO);
        assertEquals(savedDbPersonalPlanEntity.getRepeat(), REPEAT);
    }

    @Test @DisplayName("PersonalPlanEntity null 제약조건 검증")
    @Disabled
    void PersonalPlanEntity_null제약조건_검증_및_글자수_검증(){
        PersonalPlanEntity personalPlanEntity = new PersonalPlanEntity();

        assertThrows(ConstraintViolationException.class, ()->{
            personalPlanRepo.save(personalPlanEntity);
        });
    }

    @Test @DisplayName("PersonalPlanEntity 글자수 제약조건 검증")
    @Disabled
    void PersonalPlanEntity_글자수_제약조건_검증(){
        PersonalPlanEntity personalPlanEntityBlankPlanName = PersonalPlanEntity.builder()
                .planName("")
                .build();

        assertThrows(ConstraintViolationException.class, ()->{
            personalPlanRepo.save(personalPlanEntityBlankPlanName);
        });

        PersonalPlanEntity personalPlanEntityPlanNameOver30 = PersonalPlanEntity.builder()
                .planName("aeifjhsadfjpaoisefjapsioefjoisaefjoisaepjfasoiefjiaosefjaosiefjoiasejfijasfihaseoihasefhafsdlk")
                .build();

        assertThrows(ConstraintViolationException.class, ()->{
            personalPlanRepo.save(personalPlanEntityPlanNameOver30);
        });
        assertEquals(personalPlanEntityPlanNameOver30.getPlanName().length() > 30, true);
    }
}