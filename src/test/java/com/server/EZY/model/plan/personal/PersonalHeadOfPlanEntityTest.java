package com.server.EZY.model.plan.personal;

import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.headOfPlan.enumType.PlanDType;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.enumType.Permission;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.headOfPlan.repository.HeadOfPlanRepository;
import com.server.EZY.model.plan.team.repository.TeamPlanRepository;
import com.server.EZY.model.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Rollback(value = true) // update 쿼리 확인할떄 false
class PersonalHeadOfPlanEntityTest {

    @Autowired private PersonalPlanRepository personalPlanRepo;
    @Autowired UserRepository userRepo;
    @Autowired HeadOfPlanRepository headOfPlanRepository;
    @Autowired TeamPlanRepository teamPlanRepo;
    @PersistenceContext EntityManager em;

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
        HeadOfPlanEntity headOfPlanEntity = new HeadOfPlanEntity(
                userEntity
                ,personalPlanEntity
                ,categories
        );

        // When
        HeadOfPlanEntity savedHeadOfPlanEntity = headOfPlanRepository.save(headOfPlanEntity);

        UserEntity getUserEntity = savedHeadOfPlanEntity.getUserEntity();
        PlanDType getPlanDType = savedHeadOfPlanEntity.getPlanDType();
        PersonalPlanEntity getPersonalPlanEntity = savedHeadOfPlanEntity.getPersonalPlanEntity();
        TeamPlanEntity getTeamPlanEntity = savedHeadOfPlanEntity.getTeamPlanEntity();
        List<String> getCategories = savedHeadOfPlanEntity.getCategories();

        // Then
        assertEquals(getUserEntity, userEntity);
        assertEquals(getPlanDType, PlanDType.PERSONAL_PLAN);
        assertEquals(getPersonalPlanEntity, personalPlanEntity);
        assertEquals(getTeamPlanEntity, null);
        assertEquals(getCategories.get(0), categories.get(0));
    }

    @Test @DisplayName("UserA를 통한 PersonalPlanEntity 조회 검증")
    void PersonalPlan_조회_검증(){
        // Given
        UserEntity userA = userEntityInit();
        PersonalPlanEntity userAPersonalPlan1 = personalPlanEntityInit();
        PersonalPlanEntity userAPersonalPlan2 = personalPlanEntityInit();
        List<HeadOfPlanEntity> userAPlans = new ArrayList<>(Arrays.asList(
                new HeadOfPlanEntity[] {new HeadOfPlanEntity(userA, userAPersonalPlan1), new HeadOfPlanEntity(userA, userAPersonalPlan2)})
        );
        headOfPlanRepository.saveAll(userAPlans);

        UserEntity userB = userEntityInit();
        TeamPlanEntity userABTeamPlan = teamPlanEntityInit(userB);
        List<HeadOfPlanEntity> ABTeamPlan = new ArrayList<>(Arrays.asList(
                new HeadOfPlanEntity[] {new HeadOfPlanEntity(userA, userABTeamPlan), new HeadOfPlanEntity(userB, userABTeamPlan)}
        ));
        headOfPlanRepository.saveAll(ABTeamPlan);

        // When
        List<HeadOfPlanEntity> savedUserAPlans = headOfPlanRepository.findAllPersonalPlanByUserEntityAndPersonalPlanEntityNotNull(userA);

        // Than
        int savedUserAPlansSize = savedUserAPlans.size();
        HeadOfPlanEntity savedUserAPlan1 = savedUserAPlans.get(0);
        PersonalPlanEntity savedUserAPersonalPlan1 = savedUserAPlan1.getPersonalPlanEntity();

        HeadOfPlanEntity savedUserAPlan2 = savedUserAPlans.get(1);
        PersonalPlanEntity savedUserAPersonalPlan2 = savedUserAPlan2.getPersonalPlanEntity();

        assertEquals(savedUserAPlansSize, 2);
        assertEquals(savedUserAPersonalPlan1, userAPersonalPlan1);
        assertEquals(savedUserAPersonalPlan2, userAPersonalPlan2);
    }

    @Test @DisplayName("PersonalPlan 업데이트 검증 (rollBack=true시 update 쿼리가 날라가지 않음)")
    @Transactional
    void PersonalPlan_업데이트_검증(){
        // Given
        UserEntity userEntity = userEntityInit();
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        HeadOfPlanEntity headOfPlanEntity = new HeadOfPlanEntity(userEntity, personalPlanEntity);
        HeadOfPlanEntity savedHeadOfPlanEntity = headOfPlanRepository.saveAndFlush(headOfPlanEntity);
        em.clear();

        PersonalPlanEntity beforeUpdatePersonalPlanEntity = headOfPlanEntity.getPersonalPlanEntity();

        // When
        savedHeadOfPlanEntity = headOfPlanRepository.getById(savedHeadOfPlanEntity.getHeadOfPlanIdx());
        savedHeadOfPlanEntity.getPersonalPlanEntity().updatePersonalPlan(
                PersonalPlanUpdateDto.builder()
                        .planName("변경된 일정 이름")
                        .when(Calendar.getInstance())
                        .who("너 나 둘이")
                        .where("광주소프트웨어공고")
                        .repeat(true)
                        .build()
                .toEntity()
        );
        PersonalPlanEntity updatedPersonalPlanEntity = savedHeadOfPlanEntity.getPersonalPlanEntity();

        // Then
        assertNotEquals(beforeUpdatePersonalPlanEntity.getPlanName(), updatedPersonalPlanEntity.getPlanName());
        assertNotEquals(beforeUpdatePersonalPlanEntity.getWhen(), updatedPersonalPlanEntity.getWhen());
        assertNotEquals(beforeUpdatePersonalPlanEntity.getWho(), updatedPersonalPlanEntity.getWho());
        assertNotEquals(beforeUpdatePersonalPlanEntity.getWhere(), updatedPersonalPlanEntity.getWhere());
        assertNotEquals(beforeUpdatePersonalPlanEntity.getRepeat(), updatedPersonalPlanEntity.getRepeat());
    }

    @Test @DisplayName("PersonalPlan 삭제 검증")
    @Transactional
    void PersonalPlan_삭제_검증(){
        // Given
        UserEntity userEntity = userEntityInit();
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        HeadOfPlanEntity headOfPlanEntity = new HeadOfPlanEntity(userEntity, personalPlanEntity);
        HeadOfPlanEntity savedHeadOfPlanEntity = headOfPlanRepository.saveAndFlush(headOfPlanEntity);
        em.clear();

        PersonalPlanEntity beforeUpdatePersonalPlanEntity = headOfPlanEntity.getPersonalPlanEntity();

        // When
        headOfPlanRepository.delete(headOfPlanEntity);
        Throwable planNotFoundException = assertThrows(Throwable.class,
                () -> headOfPlanRepository.findById(savedHeadOfPlanEntity.getHeadOfPlanIdx()).get()
        );

        // Then
        assertEquals(planNotFoundException.getClass(), NoSuchElementException.class);
    }
}