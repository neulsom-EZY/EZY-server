package com.server.EZY.model.plan.team;

import com.server.EZY.model.plan.team.dto.TeamPlanUpdateDto;
import com.server.EZY.model.plan.plan.PlanDType;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.user.Permission;
import com.server.EZY.model.user.Role;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Rollback(value = true) // update 쿼리 확인할떄 false
class TeamPlanEntityTest {

    @Autowired UserRepository userRepo;
    @Autowired PlanRepository planRepo;
    @Autowired PersonalPlanRepository personalPlanRepo;
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

    @Test @DisplayName("UserA를 통한 TeamPlanEntity 조회 검증")
    void TeamPlan_조회_검증(){
        // Given
        // 유저A는 개인일정, A의 팀일정, B와 함깨하는 팀일정이 있다.
        UserEntity userA = userEntityInit();
        TeamPlanEntity userATeamPlan = teamPlanEntityInit(userA);
        PersonalPlanEntity userAPersonalPlan = personalPlanEntityInit();
        List<PlanEntity> userAPlans = new ArrayList<>(Arrays.asList(
                new PlanEntity[]{new PlanEntity(userAPersonalPlan, userA), new PlanEntity(userATeamPlan, userA)})
        );
        planRepo.saveAll(userAPlans);


        UserEntity userB = userEntityInit();
        TeamPlanEntity userABTeamPlan = teamPlanEntityInit(userB);
        List<PlanEntity> ABTeamPlan = new ArrayList<>(Arrays.asList(
                new PlanEntity[] {new PlanEntity(userABTeamPlan, userA), new PlanEntity(userABTeamPlan, userB)}
        ));
        planRepo.saveAll(ABTeamPlan);

        // When
        List<PlanEntity> savedUserAPlans = planRepo.findAllTeamPlanByUserEntityAndTeamPlanEntityNotNull(userA);

        int savedUserAPlansSize = savedUserAPlans.size();
        PlanEntity savedUserAPlan = savedUserAPlans.get(0);
        TeamPlanEntity savedUserATeamPlan = savedUserAPlan.getTeamPlanEntity();
        PlanEntity savedUserABPlan = savedUserAPlans.get(1);
        TeamPlanEntity savedUserABTeamPlan = savedUserABPlan.getTeamPlanEntity();

        PlanEntity savedUserBPlan = planRepo.findAllTeamPlanByUserEntityAndTeamPlanEntityNotNull(userB).get(0);


        // Than
        assertEquals(savedUserAPlansSize, 2);

        // ATeam 검증
        assertEquals(savedUserAPlan.getPlanDType(), PlanDType.TEAM_PLAN);
        assertEquals(savedUserATeamPlan, userATeamPlan);
        assertEquals(savedUserATeamPlan.getTeamLeader(), userA);

        //ABTeam검증
        assertEquals(savedUserABPlan.getPlanDType(), PlanDType.TEAM_PLAN);
        assertEquals(savedUserABTeamPlan, userABTeamPlan);
        assertEquals(savedUserABTeamPlan.getTeamLeader(), userB);

        // A와 B가 함께 있는 Team
        assertEquals(savedUserBPlan.getTeamPlanEntity(), savedUserABTeamPlan);
    }

    @Test @DisplayName("TeamPlan업데이트 검증(rollBack=true시 update 쿼리가 날라가지 않음)")
    void TeamPlan_업데이트_검증() throws Exception {
        // Given
        UserEntity userEntityTeamLeader = userEntityInit();
        TeamPlanEntity beforeUpdatedTeamPlanEntity = teamPlanEntityInit(userEntityTeamLeader);
        PlanEntity savedTeamPlan = planRepo.saveAndFlush(new PlanEntity(beforeUpdatedTeamPlanEntity, userEntityTeamLeader));
        em.clear();

        // When
        savedTeamPlan = planRepo.getById(savedTeamPlan.getPlanIdx());
        savedTeamPlan.getTeamPlanEntity().updateTeamPlan(
                TeamPlanUpdateDto.builder()
                        .planName("변경한 TeamPlan")
                        .what("변경할 TeamPlan")
                        .where("광주소프트웨어공고")
                        .when(Calendar.getInstance())
                        .build()
                .toEntity(),
                userEntityTeamLeader // 팀리더만 teamEntity 팀리더를 Entity에 넘겨주어 검증후 업데이트 된다.
        );
        TeamPlanEntity updatedTeamPlanEntity = savedTeamPlan.getTeamPlanEntity();

        // Then
        assertNotEquals(beforeUpdatedTeamPlanEntity.getPlanName(), updatedTeamPlanEntity.getPlanName());
        assertNotEquals(beforeUpdatedTeamPlanEntity.getWhat(), updatedTeamPlanEntity.getWhat());
        assertNotEquals(beforeUpdatedTeamPlanEntity.getWhen(), updatedTeamPlanEntity.getWhen());
        assertNotEquals(beforeUpdatedTeamPlanEntity.getWhere(), updatedTeamPlanEntity.getWhere());
        assertEquals(beforeUpdatedTeamPlanEntity.getTeamLeader().getUserIdx(), updatedTeamPlanEntity.getTeamLeader().getUserIdx());
    }

    @Test @DisplayName("팀원이 TeamPlan업데이트시 예외 검증")
    void TeamPlan_업데이트_예외_팀맴버가_수정할경우_검증() throws Exception {
        // Given
        UserEntity teamLeader = userEntityInit();
        UserEntity teamMemberA = userEntityInit();
        TeamPlanEntity beforeUpdatedTeamPlanEntity = teamPlanEntityInit(teamLeader);
        planRepo.saveAndFlush(new PlanEntity(beforeUpdatedTeamPlanEntity, teamLeader));
        PlanEntity savedMemberATeamPlanEntity = planRepo.saveAndFlush(new PlanEntity(beforeUpdatedTeamPlanEntity, teamMemberA));
        em.clear();

        // When
        savedMemberATeamPlanEntity = planRepo.getById(savedMemberATeamPlanEntity.getPlanIdx());
        PlanEntity finalSavedMemberATeamPlanEntity = savedMemberATeamPlanEntity; // 람다에서는 final 혹은 원본값을 copy한 유사 final을 사용해야 하므로
        Throwable savedTeamPlanException = assertThrows(Exception.class, () ->
                finalSavedMemberATeamPlanEntity.getTeamPlanEntity().updateTeamPlan(
                        TeamPlanUpdateDto.builder()
                                .planName("변경한 TeamPlan")
                                .what("변경할 TeamPlan")
                                .where("광주소프트웨어공고")
                                .when(Calendar.getInstance())
                                .build()
                                .toEntity()
                        ,teamMemberA
                )
        );
        TeamPlanEntity updatedTeamPlanEntity = finalSavedMemberATeamPlanEntity.getTeamPlanEntity();

        // Then
        assertEquals(savedTeamPlanException.getClass(), Exception.class);
        assertEquals(beforeUpdatedTeamPlanEntity.getPlanName(), updatedTeamPlanEntity.getPlanName());
        assertEquals(beforeUpdatedTeamPlanEntity.getWhat(), updatedTeamPlanEntity.getWhat());
        assertEquals(beforeUpdatedTeamPlanEntity.getWhen(), updatedTeamPlanEntity.getWhen());
        assertEquals(beforeUpdatedTeamPlanEntity.getWhere(), updatedTeamPlanEntity.getWhere());
        assertEquals(beforeUpdatedTeamPlanEntity.getTeamLeader().getUserIdx(), updatedTeamPlanEntity.getTeamLeader().getUserIdx());
    }

    @Test @DisplayName("TeamPlanEntity삭제 검증")
    void teamPlanEntity_삭제_검증(){
        // Given
        UserEntity teamLeader = userEntityInit();
        UserEntity teamMemberA = userEntityInit();
        TeamPlanEntity beforeUpdatedTeamPlanEntity = teamPlanEntityInit(teamLeader);
        planRepo.saveAndFlush(new PlanEntity(beforeUpdatedTeamPlanEntity, teamLeader));
        PlanEntity savedMemberATeamPlanEntity = planRepo.saveAndFlush(new PlanEntity(beforeUpdatedTeamPlanEntity, teamMemberA));
        em.clear();

        // When
        savedMemberATeamPlanEntity = planRepo.getById(savedMemberATeamPlanEntity.getPlanIdx());
        planRepo.deleteById(savedMemberATeamPlanEntity.getPlanIdx());

        PlanEntity finalSavedMemberATeamPlanEntity = savedMemberATeamPlanEntity; // 람다에서는 final 혹인 원본값을 copy한 유사 final을 사용해야 하므로
        Throwable deleteException = assertThrows(EmptyResultDataAccessException.class,
                () -> planRepo.deleteById(finalSavedMemberATeamPlanEntity.getPlanIdx())
        );

        // Then
        assertEquals(deleteException.getClass(), EmptyResultDataAccessException.class);
    }
}