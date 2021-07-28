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
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

        ).limit(5).collect(Collectors.toList());

        // When
        personalPlanRepository.saveAll(personalPlanEntities);
        List<PersonalPlanEntity> allPersonalPlan = personalPlanService.getAllPersonalPlan();

        // then
        assertTrue(allPersonalPlan.size() == 5);
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

        ).limit(5).collect(Collectors.toList());

        // When
        List<PersonalPlanEntity> planEntities = personalPlanRepository.saveAll(personalPlanEntities);
        PersonalPlanEntity thisPersonalPlan = personalPlanService.getThisPersonalPlan(planEntities.get(3).getPlanIdx());

        // Then
        assertTrue(thisPersonalPlan.getPlanInfo().getExplanation() == "오하이오");
    }

    @Order(2)
    @Test @DisplayName("단건 개인일정 삭제가 가능한가요?")
    void deleteThisPersonalPlan(){
        //Given
        List<PersonalPlanEntity> personalPlanEntities = Stream.generate(
                () -> PersonalPlanEntity.builder()
                        .memberEntity(savedMemberEntity)
                        .repetition(false)
                        .period( new Period(
                                        LocalDateTime.of(2021, 7, 24, 1, 30),
                                        LocalDateTime.of(2021, 7, 24, 1, 30)
                                )
                        ).planInfo(new PlanInfo(RandomStringUtils.randomAlphabetic(10), "오하이오")).build()

        ).limit(5).collect(Collectors.toList());

        //When
        List<PersonalPlanEntity> planEntities = personalPlanRepository.saveAll(personalPlanEntities);
        personalPlanService.deleteThisPersonalPlan(personalPlanEntities.get(3).getPlanIdx());

        //Then
        assertTrue(personalPlanRepository.findAll().size() == 4);
    }

    @Order(1)
    @Test @DisplayName("단건 개일일정 변경이 가능한가요?")
    void updateThisPersonalPlan() throws Exception {
        //Given
        List<PersonalPlanEntity> personalPlanEntities = Stream.generate(
                () -> PersonalPlanEntity.builder()
                        .memberEntity(savedMemberEntity)
                        .repetition(false)
                        .period( new Period(
                                        LocalDateTime.of(2021, 7, 24, 1, 30),
                                        LocalDateTime.of(2021, 7, 24, 1, 30)
                                )
                        ).planInfo(new PlanInfo(RandomStringUtils.randomAlphabetic(10), "오하이오")).build()

        ).limit(5).collect(Collectors.toList());

        //When
        List<PersonalPlanEntity> planEntities = personalPlanRepository.saveAll(personalPlanEntities);
        PersonalPlanEntity updatedPersonalPlan = personalPlanService.updateThisPersonalPlan(
                3L,
                PersonalPlanSetDto.builder()
                        .repetition(true)
                        .period(new Period(
                                LocalDateTime.of(2021, 2, 12, 1, 30),
                                LocalDateTime.of(2021, 1, 25, 1, 30)
                        ))
                        .planInfo(new PlanInfo("hello world", "이거 수정됨 ㅎ"))
                        .build()
        );

        //Then
        assertTrue(updatedPersonalPlan.getPlanInfo().getTitle().equals("hello world"));
    }
}