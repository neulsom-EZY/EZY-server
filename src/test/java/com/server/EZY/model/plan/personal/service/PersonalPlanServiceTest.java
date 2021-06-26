package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.plan.PlanEntity;
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
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PersonalPlanServiceTest {

    @Autowired
    private PersonalPlanService personalPlanService;
    @Autowired
    private PersonalPlanRepository personalPlanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Test @DisplayName("개인일정 추가 - category.size == 0")
    public void 개인일정추가(){
        /**
         * Given 1st
         * 이 부분은 PersonalPlanService/savePersonalPlan 에 myPersonalPlan 을 세트하는 부분입니다.
         */
        PersonalPlanDto myPersonalPlan = PersonalPlanDto.builder()
                .planName("지환이랑 놀기")
                .when(Calendar.getInstance())
                .where("롯데월드")
                .what("리프레시 타임")
                .who("시원이, 태현이")
                .repeat(false)
                .build();
        /**
         * Given 2nd
         * 이 List는 savePersonalPlan 메서드 파라미터 category 를 정의한 부분입니다.
         */
        List<String> personalPlanCategory = new ArrayList<>();

        /**
         * When
         * personalPlanService.savePersonalPlan 에 알맞는 param 값을 넘겨줍니다.
         * -> 수행 됐다면 result 에 저장된 Entity 값이 담기겠지요.
         */
        PlanEntity result = personalPlanService.savePersonalPlan(myPersonalPlan, personalPlanCategory);

        /**
         * Then
         * -> categroy에 아무것도 저장한 내용이 없으니.. size expected:0 이 Equals로 true 입니다!
         */
        assertEquals(0, result.getCategories().size());
    }
}