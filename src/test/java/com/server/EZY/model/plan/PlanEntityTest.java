package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.plan.enumType.PlanDType;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.enumType.Permission;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.plan.repository.PlanRepository;
import com.server.EZY.model.plan.team.repository.TeamPlanRepository;
import com.server.EZY.model.user.repository.UserRepository;
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

    // Test 편의를 위한 personalPlanEntity 생성
    PersonalPlanEntity personalPlanEntityInit(){
        return PersonalPlanEntity.builder()
                .planName(RandomString.make(10))
                .what(RandomString.make(20))
                .who(RandomString.make(20))
                .when(Calendar.getInstance())
                .where(RandomString.make(20))
                .repeat(false)
                .build();
    }

    // Test 편의를 위한 teamPlanEntity 생성
    TeamPlanEntity teamPlanEntityInit(UserEntity leader){
        return TeamPlanEntity.builder()
                .teamLeader(leader)
                .planName(RandomString.make(10))
                .what(RandomString.make(20))
                .when(Calendar.getInstance())
                .where(RandomString.make(20))
                .build();
    }

    // Test 편의를 위한 유저 생성 userEntityInit
    UserEntity userEntityInit(){
        UserEntity user = UserEntity.builder()
                .nickname(RandomString.make(10))
                .password(RandomString.make(10))
                .phoneNumber("010"+ (int)(Math.random()* Math.pow(10, 8)))
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
        return userRepo.save(user);
    }


    @Test @DisplayName("PersonalPlan 를 통한 PlanEntity 생성 및 저장 테스트")
    void PlanEntity_PersonalPlanEntity_생성및저장_검증(){
        // Given
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        UserEntity userEntity = userEntityInit();

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
        TeamPlanEntity getTeamPlanEntity = savedPlanEntity.getTeamPlanEntity();
        List<String> getCategories = savedPlanEntity.getCategories();

        // Then
        assertEquals(getUserEntity, userEntity);
        assertEquals(getPlanDType, PlanDType.PERSONAL_PLAN);
        assertEquals(getPersonalPlanEntity, personalPlanEntity);
        assertEquals(getTeamPlanEntity, null);
        assertEquals(getCategories.get(0), categories.get(0));
    }

    @Test @DisplayName("PersonalPlanEntity, UserEntity, Categories 값들을 이용한 PlanEntity 생성자 Exception 검증")
    void PersonalPlan를_통해_PlanEntity생성시_null로_생성시_exception_검증(){
        // Given
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        UserEntity userEntity = userEntityInit();

        PersonalPlanEntity nullPersonalPlanEntity = null;
        UserEntity nullUserEntity = null;

        final List<String> CATEGORIES = Collections.singletonList(RandomString.make(10));
        // When
        Throwable planConstructException1 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(nullPersonalPlanEntity, userEntity, CATEGORIES)
        );
        Throwable planConstructException2 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(personalPlanEntity, nullUserEntity, CATEGORIES)
        );
        Throwable planConstructException3 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(nullPersonalPlanEntity, nullUserEntity, CATEGORIES)
        );
        Throwable planConstructException4 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(personalPlanEntity, userEntity, null)
        );

        // Then
        assertEquals(planConstructException1.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException2.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException3.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException4.getClass(), IllegalArgumentException.class);
    }

    @Test @DisplayName("TeamPlanEntity 를 통한 PlanEntity 생성 및 저장 테스트")
    void PlanEntity_TeamPlanEntity_생성및저장_검증(){
        // Given
        UserEntity userAEntity = userEntityInit();
        TeamPlanEntity teamPlanEntity = teamPlanEntityInit(userAEntity);

        List<String> categories = Collections.singletonList("공부");
        PlanEntity planEntity = new PlanEntity(teamPlanEntity, userAEntity, categories);

        // When
        PlanEntity savedPlanEntity = planRepo.save(planEntity);

        UserEntity getUserEntity = savedPlanEntity.getUserEntity();
        PlanDType getPlanDType = savedPlanEntity.getPlanDType();
        PersonalPlanEntity getPersonalPlanEntity = savedPlanEntity.getPersonalPlanEntity();
        TeamPlanEntity getTeamPlanEntity = savedPlanEntity.getTeamPlanEntity();
        UserEntity getTeamLeader = savedPlanEntity.getTeamPlanEntity().getTeamLeader();
        List<String> getCategories = savedPlanEntity.getCategories();

        // Then
        assertEquals(getUserEntity, userAEntity);
        assertEquals(getPlanDType, PlanDType.TEAM_PLAN);
        assertEquals(getPersonalPlanEntity, null);
        assertEquals(getTeamPlanEntity, teamPlanEntity);
        assertEquals(getTeamLeader, userAEntity);
        assertEquals(getCategories.get(0), categories.get(0));
    }

    @Test @DisplayName("TeamPlanEntity, UserEntity, Categories 값들을 이용한 PlanEntity 생성자 Exception 검증")
    void TeamPlanEntity를_통해_PlanEntity생성시_null로_생성시_exception_검증(){
        // Given
        UserEntity userEntity = userEntityInit();
        TeamPlanEntity teamPlanEntity = teamPlanEntityInit(userEntity);

        TeamPlanEntity nullTeamPlanEntity = null;
        UserEntity nullUserEntity = null;

        final List<String> CATEGORIES = Collections.singletonList(RandomString.make(10));

        // When
        IllegalArgumentException planConstructException1 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(nullTeamPlanEntity, userEntity, CATEGORIES)
        );
        IllegalArgumentException planConstructException2 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(teamPlanEntity, nullUserEntity, CATEGORIES)
        );
        IllegalArgumentException planConstructException3 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(nullTeamPlanEntity, nullUserEntity, CATEGORIES)
        );
        IllegalArgumentException planConstructException4 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(teamPlanEntity, userEntity, null)
        );

        // Then
        assertEquals(planConstructException1.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException2.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException3.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException4.getClass(), IllegalArgumentException.class);
    }
}