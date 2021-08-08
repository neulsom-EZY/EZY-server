package com.server.EZY.model.plan.team;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.embeddedTypes.Color;
import com.server.EZY.model.plan.tag.repository.TagRepository;
import com.server.EZY.model.plan.team.repository.team.TeamRepository;
import com.server.EZY.model.plan.team.repository.teamPlan.TeamPlanRepository;
import com.server.EZY.testConfig.QueryDslTestConfig;
import edu.emory.mathcs.backport.java.util.Collections;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Import(QueryDslTestConfig.class)
class TeamEntityTest {
    @Autowired TeamRepository teamRepository;
    @Autowired TeamPlanRepository teamPlanRepository;

    @Autowired MemberRepository memberRepository;
    @Autowired TagRepository tagRepository;

    @Autowired EntityManager em; // update,delete쿼리 확인하기 위해 사용

    MemberEntity initMember(){
        return memberRepository.save(
                MemberEntity.builder()
                        .username('@' + RandomStringUtils.randomAlphabetic(5))
                        .phoneNumber("010" + RandomStringUtils.random(8, false, true))
                        .password(RandomStringUtils.randomAlphabetic(8))
                        .roles(Collections.singletonList(Role.ROLE_CLIENT))
                        .build()
        );
    }

    TagEntity tagInit(MemberEntity teamLeader){
        return tagRepository.save(
                TagEntity.builder()
                        .memberEntity(teamLeader)
                        .tag(RandomStringUtils.randomAlphabetic(8))
                        .color(new Color((short)123, (short)123, (short)123))
                        .build()
        );
    }

    TeamPlanEntity initTeamPlan(MemberEntity memberEntity){
        TagEntity tagEntity = tagInit(memberEntity);
        PlanInfo planInfo = PlanInfo.builder()
                .title("JVM뿌셔!")
                .explanation("JAVA를 동작할 수 있게 하는 JVM을 공부해보자")
                .build();
        Period period = new Period(
                LocalDateTime.of(2021,8,7,1,30),
                LocalDateTime.of(2021, 8, 8, 1, 30)
        );
        return teamPlanRepository.save(
                TeamPlanEntity.builder()
                        .memberEntity(memberEntity)
                        .planInfo(planInfo)
                        .period(period)
                        .tagEntity(tagEntity)
                        .build()
        );
    }


    @Test @DisplayName("팀일정 저장, 조회 테스트")
    public void 팀일정_저장_조회_테스트(){
        /*
          Given
          팀 리더(TeamLeader)와 팀 구성원 A(memberA)를 생성후 팀 일정관련 정보를 정보 추가(태그는 TeamLeader만 지정가능)
          초기 TeamEntity + TeamPlanEntity 생성은 Leader먼저 생성된다.
         */
        MemberEntity teamLeader = initMember();
        MemberEntity teamMemberA = initMember();

        TagEntity tagEntity = tagInit(teamLeader);
        PlanInfo planInfo = PlanInfo.builder()
                .title("JVM뿌셔!")
                .explanation("JAVA를 동작할 수 있게 하는 JVM을 공부해보자")
                .build();
        Period period = new Period(
                LocalDateTime.of(2021,8,7,1,30),
                LocalDateTime.of(2021, 8, 8, 1, 30)
        );

        TeamPlanEntity teamPlanEntity = TeamPlanEntity.builder()
                .memberEntity(teamLeader)
                .tagEntity(tagEntity)
                .planInfo(planInfo)
                .period(period)
                .build();

        /*
        When
        1. TeamPlanEntity와 TeamPlan을 MemberLeader와 연관관계를 맺어 저장한다.
        2. 1번과 다른TeamPlanEntity에 memberA와 teamLeader가 만든 TeamPlan에 연관관계를 맺어 저장한다.
         > teamEntity_TeamLeader.teamPlanEntity == teamEntity_memberA.teamPlanEntity이므로
         > teamPlanEntity만 알고 있다면 해당 TeamPlan의 구성원을 알 수 있다.
        3. TeamPlanEntity로 TeamEntity에서 팀원을 select한다.
         > 무조건 결과가2개 나와야 되며 teamLeader와 member_A가 select되야한다.
        */
        teamPlanEntity = teamPlanRepository.save(teamPlanEntity);
        TeamEntity teamEntity_TeamLeader = TeamEntity.builder()
                .memberEntity(teamLeader)
                .teamPlanEntity(teamPlanEntity)
                .build();

        teamEntity_TeamLeader = teamRepository.save(teamEntity_TeamLeader);
        TeamEntity teamEntity_memberA = teamRepository.save( //memberA를 teamLeader의 팀에 추가시킨다.
                TeamEntity.builder()                        //(MemberEntity와 TeamPlanEntity는 TeamEntity이라는 관계로 연관관계를 맺는다)
                        .memberEntity(teamMemberA)
                        .teamPlanEntity(teamPlanEntity)
                        .build()
        );
        List<TeamEntity> findAllPlanEntities = teamRepository.findAllByTeamPlanEntity(teamPlanEntity);

        // Than
        assertEquals(tagEntity, teamEntity_TeamLeader.getTeamPlanEntity().getTagEntity());
        assertEquals(planInfo, teamEntity_TeamLeader.getTeamPlanEntity().getPlanInfo());
        assertEquals(period, teamEntity_TeamLeader.getTeamPlanEntity().getPeriod());
        // teamLeader와 memberA의 TeamPlanEntity는 같아야 한다. (같은 TeamPlanEntity와 연관관계를 맺었으므로)
        assertEquals(teamEntity_TeamLeader.getTeamPlanEntity(), teamEntity_memberA.getTeamPlanEntity());

        assertEquals(2, findAllPlanEntities.size());
        for(TeamEntity t : findAllPlanEntities) {
            if(!(t.getMemberEntity() != teamLeader || t.getMemberEntity() != teamMemberA))
                fail("조회한 T teamLeader, memberA가 저장된 MemberEntity가 있어야 합니다.");
        }
    }

    @Test @DisplayName("팀 일정내용 수정 테스트")
//    @Transactional // SpringBootTest 활용해서 DB값 확인하려함
    void 팀_일정내용_수정_테스트(){
        // Given
        MemberEntity memberEntity = initMember();

        //팀 일정 생성(리더만)
        TeamPlanEntity teamPlanEntity = initTeamPlan(memberEntity);
        teamRepository.save(
                TeamEntity.builder()
                        .memberEntity(memberEntity)
                        .teamPlanEntity(teamPlanEntity)
                        .build()
        );

        /*
        변경할 일정값 planInfo + period = updateTeamPlanData
         */
        PlanInfo planInfo = PlanInfo.builder() //location 추가 예정이므로 버그 고치기 귀찮아 설정
                .title("변경된 팀 일정 정보")
                .explanation("변경된 팀 일정")
                .build();
        Period period = new Period(
                LocalDateTime.of(2021, 9, 3, 1, 30),
                LocalDateTime.of(2021, 9, 4, 1, 39)
        );
        TeamPlanEntity updateTeamPlanData = TeamPlanEntity.builder()
                .planInfo(planInfo)
                .period(period)
                .build();

        // When
        teamPlanEntity.updateTeamPlan(updateTeamPlanData);

        // Then
        em.flush(); //update query 확인용도

        assertEquals(planInfo, updateTeamPlanData.getPlanInfo());
        assertEquals(period, updateTeamPlanData.getPeriod());
    }

    @Test @DisplayName("팀 일정 삭제 테스트")
//    @Transactional
    void 팀_일정내용_삭제_테스트(){
        // Given
        MemberEntity teamLeader = initMember();
        MemberEntity member_A = initMember();

        //팀 일정 생성
        TeamPlanEntity teamPlanEntity = initTeamPlan(teamLeader);
        teamRepository.save(
                TeamEntity.builder()
                        .memberEntity(teamLeader)
                        .teamPlanEntity(teamPlanEntity)
                        .build()
        );
        teamRepository.save(
                TeamEntity.builder()
                        .memberEntity(member_A)
                        .teamPlanEntity(teamPlanEntity)
                        .build()
        );

        /*
        1. 현재 TeamPlanEntity와 연관관계를 맻고있는 TeamEntity를 모두 찾아 삭제한다.
        2. 해당 TeamPlan을 삭제한다.
         */
        teamRepository.deleteAllByTeamPlanEntity(teamPlanEntity);
        teamPlanRepository.delete(teamPlanEntity);

        em.flush(); // delete쿼리 확인 및 Then 에서 select쿼리를 통해 delete됐는지 확인하기 위한 용도

        // Then
        assertThrows(
                NoSuchElementException.class,
                () -> teamPlanRepository.findById(teamPlanEntity.getPlanIdx()).get()
        );
        assertEquals(0, teamRepository.findAllByTeamPlanEntity(teamPlanEntity).size());
    }

}