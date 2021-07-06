package com.server.EZY.model.plan.team.service;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.headOfPlan.repository.HeadOfPlanRepository;
import com.server.EZY.model.plan.team.dto.TeamPlanDto;
import com.server.EZY.model.plan.team.repository.TeamPlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
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
import java.util.List;

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

}