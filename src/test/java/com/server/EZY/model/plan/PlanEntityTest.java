package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
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
        IllegalArgumentException planConstructException1 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(nullPersonalPlanEntity, userEntity, CATEGORIES)
        );
        IllegalArgumentException planConstructException2 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(personalPlanEntity, nullUserEntity, CATEGORIES)
        );
        IllegalArgumentException planConstructException3 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(nullPersonalPlanEntity, nullUserEntity, CATEGORIES)
        );
        IllegalArgumentException planConstructException4 = assertThrows(IllegalArgumentException.class
                , () -> new PlanEntity(personalPlanEntity, userEntity, null)
        );

        // Then
        assertEquals(planConstructException1.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException2.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException3.getClass(), IllegalArgumentException.class);
        assertEquals(planConstructException4.getClass(), IllegalArgumentException.class);
    }

    @Test @DisplayName("UserA를 통한 PersonalPlanEntity 조회 검증")
    void PersonalPlan_조회_검증(){
        // Given
        UserEntity userA = userEntityInit();
        PersonalPlanEntity userAPersonalPlan1 = personalPlanEntityInit();
        PersonalPlanEntity userAPersonalPlan2 = personalPlanEntityInit();
        List<PlanEntity> userAPlans = new ArrayList<>(Arrays.asList(
                new PlanEntity[] {new PlanEntity(userAPersonalPlan1, userA), new PlanEntity(userAPersonalPlan2, userA)})
        );
        planRepo.saveAll(userAPlans);

        UserEntity userB = userEntityInit();
        TeamPlanEntity userABTeamPlan = teamPlanEntityInit(userB);
        List<PlanEntity> ABTeamPlan = new ArrayList<>(Arrays.asList(
                new PlanEntity[] {new PlanEntity(userABTeamPlan, userA), new PlanEntity(userABTeamPlan, userB)}
        ));
        planRepo.saveAll(ABTeamPlan);

        // When
        List<PlanEntity> savedUserAPlans = planRepo.findAllByUserEntityAndPersonalPlanEntityNotNull(userA);

        // Than
        int savedUserAPlansSize = savedUserAPlans.size();
        PlanEntity savedUserAPlan1 = savedUserAPlans.get(0);
        PersonalPlanEntity savedUserAPersonalPlan1 = savedUserAPlan1.getPersonalPlanEntity();

        PlanEntity savedUserAPlan2 = savedUserAPlans.get(1);
        PersonalPlanEntity savedUserAPersonalPlan2 = savedUserAPlan2.getPersonalPlanEntity();

        assertEquals(savedUserAPlansSize, 2);
        assertEquals(savedUserAPersonalPlan1, userAPersonalPlan1);
        assertEquals(savedUserAPersonalPlan2, userAPersonalPlan2);
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