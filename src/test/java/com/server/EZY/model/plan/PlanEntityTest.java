package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.user.Permission;
import com.server.EZY.model.user.Role;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.repository.plan.PersonalPlanRepository;
import com.server.EZY.repository.plan.PlanRepository;
import com.server.EZY.repository.plan.TeamPlanRepository;
import com.server.EZY.repository.user.UserRepository;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlanEntityTest {

    @Autowired UserRepository userRepo;
    @Autowired PlanRepository planRepo;
    @Autowired PersonalPlanRepository personalPlanRepo;
    @Autowired TeamPlanRepository teamPlanRepo;

    PersonalPlanEntity personalPlanEntityInit(){
        return PersonalPlanEntity.builder()
                .planName(RandomString.make(10))
                .what(RandomString.make(20))
                .when(Calendar.getInstance())
                .where(RandomString.make(20))
                .repeat(false)
                .build();
    }

    UserEntity userEntityInit(){
        return UserEntity.builder()
                .nickname(RandomString.make(10))
                .password(RandomString.make(10))
                .phoneNumber("01012341234")
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
    }


    @Test @DisplayName("PersonalPlan 생성 및 저장 테스트")
    void PersonalPlan를_통해_PlanEntity생성_검증(){
        // Given
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        UserEntity userEntity = userEntityInit();
        userRepo.save(userEntity);

        List<String> categories = Collections.singletonList("공부");
        PlanEntity planEntity = new PlanEntity(
                personalPlanEntity
                ,userEntity
                ,categories
        );

        // When
        PlanEntity savedPlanEntity = planRepo.save(planEntity);

        UserEntity getUserEntity = savedPlanEntity.getUserEntity();
        PlanDType getPlanDType = savedPlanEntity.getPlanDType();
        PersonalPlanEntity getPersonalPlanEntity = savedPlanEntity.getPersonalPlanEntity();
        List<String> getCategories = savedPlanEntity.getCategories();


        // Then
        assertEquals(getUserEntity, userEntity);
        assertEquals(getPlanDType, PlanDType.PERSONAL_PLAN);
        assertEquals(getPersonalPlanEntity, personalPlanEntity);
        assertEquals(getCategories.get(0), categories.get(0));
    }
}