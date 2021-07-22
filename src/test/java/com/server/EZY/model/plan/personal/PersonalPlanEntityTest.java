package com.server.EZY.model.plan.personal;

import com.server.EZY.model.plan.Period;
import com.server.EZY.model.plan.PlanInfo;
import com.server.EZY.model.plan.personal.enumType.PlanDType;
import com.server.EZY.model.plan.personal.dto.NewPersonalPlanUpdateDto;
import com.server.EZY.model.plan.personal.repository.NewPersonalPlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.enumType.Permission;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.testConfig.QueryDslTestConfig;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Import(QueryDslTestConfig.class)
class PersonalPlanEntityTest {

    @Autowired NewPersonalPlanRepository personalPlanRepo;
    @Autowired UserRepository userRepo;

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

    NewPersonalPlanEntity personalPlanEntityInit(UserEntity user){
        Period period = Period.builder()
                .startTime(LocalDateTime.of(2021, 7, 20, 12, 0))
                .endTime(LocalDateTime.of(2021, 7, 20, 14, 0))
                .build();
        PlanInfo planInfo = PlanInfo.builder()
                .title("기능반 출근후 코딩")
                .explanation("간단한 코딩, 2시까지 할꺼임")
                .build();
        return new NewPersonalPlanEntity(user, planInfo, period, false);
    }

    @Test() @DisplayName("PersonalPlan equals, hashCode test")
    void equals_hashCode_test() throws Exception {
        UserEntity userEntity = userEntityInit();

        NewPersonalPlanEntity personalPlanEntity = personalPlanEntityInit(userEntity);
        NewPersonalPlanEntity clonePersonalPlanEntity = personalPlanEntityInit(userEntity);

        assertFalse(personalPlanEntity.equals(userEntity));
        assertTrue(personalPlanEntity.equals(clonePersonalPlanEntity));
        assertEquals(personalPlanEntity.hashCode(), clonePersonalPlanEntity.hashCode());

        PlanInfo updatePlanInfo = new PlanInfo("변경할 일정제목", "그냥 변경할 일정 설명");
        LocalDateTime updateStartTime = LocalDateTime.of(2022, 7, 20, 9, 0);
        LocalDateTime updateEndTime = LocalDateTime.of(2023, 7, 20, 12, 0);
        Period updatePeriod = new Period(updateStartTime, updateEndTime);
        NewPersonalPlanEntity updatePersonalPlanEntity = NewPersonalPlanUpdateDto.builder()
                .userEntity(userEntity)
                .planInfo(updatePlanInfo)
                .period(updatePeriod)
                .repetition(true)
                .build()
                .toEntity();

        clonePersonalPlanEntity.updatePersonalPlanEntity(userEntity, updatePersonalPlanEntity);
        assertFalse(personalPlanEntity.equals(clonePersonalPlanEntity));

    }

    @Test @DisplayName("PersonalPlan(개인일정) 저장, 조회 테스트")
    void PersonalPlan_저장_조회_테스트(){
        // Given
        UserEntity user = userEntityInit();

        Period period = Period.builder()
                .startTime(LocalDateTime.of(2021, 7, 20, 12, 0))
                .endTime(LocalDateTime.of(2021, 7, 20, 14, 0))
                .build();
        PlanInfo planInfo = PlanInfo.builder()
                .title("기능반 출근후 코딩")
                .explanation("간단한 코딩, 2시까지 할꺼임")
                .build();

        // When
        NewPersonalPlanEntity personalPlanEntity = new NewPersonalPlanEntity(user, planInfo, period, false);
        NewPersonalPlanEntity savedPersonalPlanEntity = personalPlanRepo.saveAndFlush(personalPlanEntity);

        NewPersonalPlanEntity selectPersonalPlanEntity = personalPlanRepo.getById(savedPersonalPlanEntity.getPersonalPlanIdx());

        // Then
        assertEquals(period, selectPersonalPlanEntity.getPeriod()); //equals로 동등성 비교
        assertEquals(planInfo, selectPersonalPlanEntity.getPlanInfo()); //equals로 동등성 비교
        assertEquals(PlanDType.PERSONAL_PLAN, selectPersonalPlanEntity.getDType());
        assertEquals(savedPersonalPlanEntity, selectPersonalPlanEntity);
        assertFalse(selectPersonalPlanEntity.getRepetition()); //repetition 필드를 false초기화 했으므로
    }


    @Test @DisplayName("PersonalPlan(개인일정) 모든필드 수정 테스트")
    void PersonalPlan_수정테스트() throws Exception {
        // Given
        UserEntity user = userEntityInit();
        NewPersonalPlanEntity oldPersonalPlanEntity = personalPlanEntityInit(user);
        NewPersonalPlanEntity saveOldPersonalPlanEntity = personalPlanRepo.save(oldPersonalPlanEntity);

        // 업데이트 정보
        PlanInfo updatePlanInfo = new PlanInfo("변경할 일정제목", "그냥 변경할 일정 설명");
        LocalDateTime updateStartTime = LocalDateTime.of(2022, 7, 20, 9, 0);
        LocalDateTime updateEndTime = LocalDateTime.of(2022, 7, 20, 12, 0);
        Period updatePeriod = new Period(updateStartTime, updateEndTime);
        NewPersonalPlanEntity updateBeforePersonalPlanEntity = NewPersonalPlanUpdateDto.builder()
                .userEntity(user)
                .planInfo(updatePlanInfo)
                .period(updatePeriod)
                .repetition(true)
                .build()
                .toEntity();

        // When
        saveOldPersonalPlanEntity.updatePersonalPlanEntity(user, updateBeforePersonalPlanEntity);

        // Then
        NewPersonalPlanEntity updatedPersonalPlanEntity = personalPlanRepo.getById(saveOldPersonalPlanEntity.getPersonalPlanIdx());
        assertEquals(updatedPersonalPlanEntity.getPlanInfo(), saveOldPersonalPlanEntity.getPlanInfo());
        assertEquals(updatedPersonalPlanEntity.getPeriod(), saveOldPersonalPlanEntity.getPeriod());
        assertEquals(updatedPersonalPlanEntity.getRepetition(), saveOldPersonalPlanEntity.getRepetition());
    }

    @Test @DisplayName("PersonalPlan(개인일정) 업데이트시 소유자가 다른 유저가 업데이트를 하게 된다면?")
    void PersonalPlan_다른소유자가_업데이트시_exception_테스트() throws Exception {
        // Given
        UserEntity userA = userEntityInit();
        UserEntity userB = userEntityInit();
        NewPersonalPlanEntity oldPersonalPlanEntity = personalPlanEntityInit(userA);
        NewPersonalPlanEntity saveOldPersonalPlanEntity = personalPlanRepo.save(oldPersonalPlanEntity);

        // 업데이트 정보
        PlanInfo updatePlanInfo = new PlanInfo("변경할 일정제목", "그냥 변경할 일정 설명");
        LocalDateTime updateStartTime = LocalDateTime.of(2022, 7, 20, 9, 0);
        LocalDateTime updateEndTime = LocalDateTime.of(2022, 7, 20, 12, 0);
        Period updatePeriod = new Period(updateStartTime, updateEndTime);
        NewPersonalPlanEntity updateBeforePersonalPlanEntity = NewPersonalPlanUpdateDto.builder()
                .userEntity(userA)
                .planInfo(updatePlanInfo)
                .period(updatePeriod)
                .repetition(true)
                .build()
                .toEntity();

        // When Then
        Throwable throwable = assertThrows(Throwable.class,
                () -> saveOldPersonalPlanEntity.updatePersonalPlanEntity(userB, updateBeforePersonalPlanEntity)
        );
    }
}