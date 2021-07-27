package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanSetDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.util.CurrentUserUtil;
import org.apache.commons.lang.RandomStringUtils;
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
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonalPlanServiceImplTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PersonalPlanService personalPlanService;
    @Autowired
    private PersonalPlanRepository personalPlanRepository;


    MemberEntity savedMemberEntity;
    @BeforeEach @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void GetUserEntity(){
        //Given
        MemberDto memberDto = MemberDto.builder()
                .username("배태현")
                .password("1234")
                .phoneNumber("01012341234")
                .build();

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        savedMemberEntity = memberRepository.save(memberDto.toEntity());
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getUsername(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUsername();
        assertEquals("배태현", currentUserNickname);
    }

    @Test @DisplayName("personalPlanSave 서비스 흐름 테스트")
    void goImplClass(){
        // Given
        PersonalPlanEntity savedPersonalPlan = personalPlanService.createPersonalPlan(
                PersonalPlanSetDto.builder()
                        .planInfo(new PlanInfo("와우껌", "좋아요"))
                        .period(new Period(
                                        LocalDateTime.of(2021, 7, 24, 1, 30),
                                        LocalDateTime.of(2021, 7, 24, 1, 30)
                                )
                        )
                        .tagIdx(1L)
                        .repetition(false)
                        .build()
        );

        // Then
        assertTrue(savedPersonalPlan.getPlanIdx() != null);

    }

    @Test @DisplayName("personalPlan 전체 조회 가능한가요?")
    void getAllMyPersonalPlan(){
        // Given
        List<PersonalPlanEntity> personalPlanEntities = Stream.generate(
                () -> PersonalPlanEntity.builder()
                .memberEntity(savedMemberEntity)
                .repetition(false)
                .period( new Period(
                            LocalDateTime.of(2021, 7, 24, 1, 30),
                            LocalDateTime.of(2021, 7, 24, 1, 30)
                        )
                ).planInfo(new PlanInfo("하이요", "오하이오")).build()

        ).limit(30).collect(Collectors.toList());

        // When
        personalPlanRepository.saveAll(personalPlanEntities);
        List<PersonalPlanEntity> allPersonalPlan = personalPlanService.getAllPersonalPlan();

        // then
        assertTrue(allPersonalPlan.size() == 30);
    }

    @Test @DisplayName("하나의 개인일정 단건조회가 가능한가요?")
    void getThisMyPersonalPlan(){
        // Given
        List<PersonalPlanEntity> personalPlanEntities = Stream.generate(
                () -> PersonalPlanEntity.builder()
                        .memberEntity(savedMemberEntity)
                        .repetition(false)
                        .period( new Period(
                                        LocalDateTime.of(2021, 7, 24, 1, 30),
                                        LocalDateTime.of(2021, 7, 24, 1, 30)
                                )
                        ).planInfo(new PlanInfo(RandomStringUtils.randomAlphabetic(10), "오하이오")).build()

        ).limit(20).collect(Collectors.toList());

        // When
        personalPlanRepository.saveAll(personalPlanEntities);
        PersonalPlanEntity thisPersonalPlan = personalPlanService.getThisPersonalPlan(3L);

        // Then
        assertTrue(thisPersonalPlan.getPlanIdx() == 3);
        assertTrue(thisPersonalPlan.getPlanInfo().getExplanation() == "오하이오");
    }
}