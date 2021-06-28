package com.server.EZY.model.plan.personal.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.service.PersonalPlanService;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.plan.repository.PlanRepository;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
import org.aspectj.lang.annotation.After;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.AfterEach;
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

import javax.persistence.Basic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlanRepositorySupportTest {
    private PlanRepositorySupport planRepositorySupport;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PersonalPlanService personalPlanService;

    @BeforeEach @DisplayName("원활한 테스트를 위해서 임시적으로 토큰을 발급해주는 메서드")
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
        userRepository.save(userDto.toEntity());
        System.out.println("======== saved =========");햣 ㄴ

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

    @AfterEach @DisplayName("Data 섞임 방지 메서드")
    public void tearDown() throws Exception{
        planRepository.deleteAllInBatch();
    }

    @DisplayName("개인일정을 랜덤으로 세트 합니다")
    public PersonalPlanDto personalPlanEntityInit(){
        return PersonalPlanDto.builder()
                .planName(RandomString.make(10))
                .what(RandomString.make(20))
                .who(RandomString.make(20))
                .when(Calendar.getInstance())
                .where(RandomString.make(20))
                .repeat(false)
                .build();
    }

    @Test @DisplayName("개인일정을 userId를 통해 다 조회할 수 있나요?")
    void 내_개인일정을_다_가져와() {
        /**
         * Given
         * 1. personalCategory 변수를 선언합니다.
         * 2. service/save 에 미리 지정한 personalPlanInit 메서드와 category 를 파라미터로 넘겨줍니다.
         */
        List<String> personalPlanCategory = new ArrayList<>();

        personalPlanService.savePersonalPlan(personalPlanEntityInit(), personalPlanCategory);

        //when
        List<PlanEntity> allByUserId = planRepositorySupport
                .findAllByUserId(userRepository.findByNickname("배태현").getUserIdx());
    }
}