package com.server.EZY.model.plan.team.service;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.headOfPlan.repository.HeadOfPlanRepository;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.plan.team.dto.TeamPlanDto;
import com.server.EZY.model.plan.team.repository.TeamPlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.enumType.Permission;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.model.user.util.CurrentUserUtil;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamPlanServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamPlanService teamPlanService;
    @Autowired
    private TeamPlanRepository teamPlanRepository;
    @Autowired
    private HeadOfPlanRepository headOfPlanRepository;
    @Autowired
    private CurrentUserUtil currentUserUtil;

    UserEntity teamLeaderEntity;
    @BeforeEach
    @DisplayName("원활한 테스트를 위해서 임시적으로 토큰을 발급해주는 메서드")
    public void 로그인_세션(){
        /**
         * Given
         * 로그인을 수행하기 위해 먼저, user 아래 정보를 참고하여 저장합니다.
         */
        UserDto userDto = UserDto.builder()
                .nickname("배태현")
                .password("1234")
                .phoneNumber("01012341234")
                .build();

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        teamLeaderEntity = userRepository.save(userDto.toEntity());
        System.out.println("======== saved =========");

        /**
         * When
         * 저장된 user를 통해 임의적으로 token 발급합니다.
         */
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDto.getNickname(),
                userDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);
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
        return userRepository.save(user);
    }

    @Test @DisplayName("팀 일정이 잘 저장 되나요?")
    public void 팀_일정_저장(){
        //Given
        TeamPlanDto teamPlan = TeamPlanDto.builder()
                .teamLeader(teamLeaderEntity)
                .planName("지환이랑 태현이랑 시원이랑 한컷")
                .what("인생네컷")
                .when(Calendar.getInstance())
                .where("동명동")
                .build();

        //when
        HeadOfPlanEntity saveTeamPlan = teamPlanService.saveTeamPlan(teamPlan);

        //Then
        assertEquals(true, teamPlanRepository.findByPlanName(teamPlan.getPlanName()) != null);
        assertEquals(true, saveTeamPlan.getUserEntity().getNickname() == teamLeaderEntity.getNickname());
    }

    @Test @DisplayName("모든 내 팀 일정이 잘 조회 되나요?")
    public void 팀_일정_전체_조회(){
        /**
         * Given
         * 1. login user를 leader 로 저장
         * 2. anotheruser를 2_leader 로 저장
         */
        UserEntity leader = currentUserUtil.getCurrentUser();
        UserEntity anotherUser = userEntityInit();
        // 내가 찾을 team_plan
        List<HeadOfPlanEntity> teamPlanEntity = Stream.generate(
                () -> new HeadOfPlanEntity(
                        leader,
                        teamPlanEntityInit(leader)
                )
        ).limit(10).collect(Collectors.toList());
        // 다른 사람의 team_plan
        List<HeadOfPlanEntity> a_teamPlanEntity = Stream.generate(
                () -> new HeadOfPlanEntity(
                        anotherUser,
                        teamPlanEntityInit(anotherUser)
                )
        ).limit(12).collect(Collectors.toList());

        headOfPlanRepository.saveAll(teamPlanEntity);
        headOfPlanRepository.saveAll(a_teamPlanEntity);

        // When
        List<HeadOfPlanEntity> myTeamPlanList= teamPlanService.getAllMyTeamPlan();

        // Then
        assertEquals(10, myTeamPlanList.size());
    }

    @Test @DisplayName("이 팀 일정이 잘 조회 되나요?")
    public void This_팀_일정_조회(){
        /**
         * Given
         * 1. 로그인 된 사용자가 10개의 팀 일정을 생성한다.
         * 2. 저장을 요청한다.
         */
        UserEntity leader = currentUserUtil.getCurrentUser();
        UserEntity anotherUser = userEntityInit();
        // 내가 찾을 team_plan
        List<HeadOfPlanEntity> teamPlanEntity = Stream.generate(
                () -> new HeadOfPlanEntity(
                        leader,
                        teamPlanEntityInit(leader)
                )
        ).limit(10).collect(Collectors.toList());

        TeamPlanDto teamPlan = TeamPlanDto.builder()
                .teamLeader(teamLeaderEntity)
                .planName("지환이랑 태현이랑 시원이랑 한컷")
                .what("인생네컷")
                .when(Calendar.getInstance())
                .where("동명동")
                .build();

        headOfPlanRepository.saveAll(teamPlanEntity);
        HeadOfPlanEntity getThisTeamPlanIdx = teamPlanService.saveTeamPlan(teamPlan);


        /**
         * When
         * 로그인 된 사용자가 해당 plan을 조회한다.
         * 내가 실제로 찾고 싶은 TeamIdx 를 넣어 Plan을 찾는다.
         */
        HeadOfPlanEntity result = teamPlanService.getThisTeamPlan(2L);
        HeadOfPlanEntity find_result = teamPlanService.getThisTeamPlan(getThisTeamPlanIdx.getHeadOfPlanIdx());

        /**
         * Then
         * 1. 내가 찾고자 하는 TeamEntity 의 PlanName 이 내가 지정한 PlanName과 같습니까?
         * 2. 내가 2L을 넣어 찾은 TeamEntity의 회원이 내가 로그인한 회원과 일치합니까?
         */
        assertEquals(true, find_result.getTeamPlanEntity().getPlanName() == "지환이랑 태현이랑 시원이랑 한컷");
        assertEquals(true, result.getUserEntity() == leader);
    }
}