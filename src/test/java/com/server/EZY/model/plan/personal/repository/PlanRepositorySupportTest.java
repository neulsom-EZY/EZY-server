package com.server.EZY.model.plan.personal.repository;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
import com.server.EZY.model.plan.personal.service.PersonalPlanService;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.plan.repository.PlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.dto.UserDto;
import com.server.EZY.model.user.enumType.Permission;
import com.server.EZY.model.user.enumType.Role;
import com.server.EZY.model.user.repository.UserRepository;
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
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;


import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class PlanRepositorySupportTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PersonalPlanService personalPlanService;
    @Autowired
    private PersonalPlanRepository personalPlanRepository;

    // BeforeEach 로 saved 된 회원 Entity를 전역에서 사용하기 위해.
    UserEntity beforeSavedUser;
    @BeforeEach @DisplayName("원활한 테스트를 위해서 임시적으로 토큰을 발급해주는 메서드")
    public void 로그인_세션(){
        /**
         * Given
         * 로그인을 수행하기 위해 먼저, user 아래 정보를 참고하여 저장합니다.
         */
        UserDto userDto = UserDto.builder()
                .nickname("짱짱짱")
                .password("1234")
                .phoneNumber("01012341234")
               .build();

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        beforeSavedUser = userRepository.save(userDto.toEntity());
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

    // Test 편의를 위한 유저 생성 userEntityInit
    UserEntity userEntity_태현(){
        UserEntity user = UserEntity.builder()
                .nickname("배태현")
                .password(RandomString.make(10))
                .phoneNumber("010"+ (int)(Math.random()* Math.pow(10, 8)))
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
        return userRepository.save(user);
    }

    // Test 편의를 위한 유저 생성 userEntityInit
    UserEntity userEntity_지환(){
        UserEntity user = UserEntity.builder()
                .nickname("전지환")
                .password(RandomString.make(10))
                .phoneNumber("010"+ (int)(Math.random()* Math.pow(10, 8)))
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
        return userRepository.save(user);
    }


    @Test @DisplayName("개인일정을 userId를 통해 다 조회할 수 있나요?")
    void 내_개인일정을_다_가져와() {
        /**
         * Given
         * 1. personalPlanEntity 는 미리 메서드화 한 planEntity Builder를 추가시킵니다.
         * 2. userEntity_t 는 태현이의 유저 정보 save 메서드를 호출합니다.
         * 3. userEntity_j 는 지환이의 유저 정보 save 메서드를 호출합니다.
         */
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        UserEntity userEntity_t = userEntity_태현();
        UserEntity userEntity_j = userEntity_지환();
        List<String> categories = Collections.singletonList("지환이와 데이트");
        // 태현이의 Plan을 12개 추가합니다.
        List<PlanEntity> planEntities = Stream.generate(
                () -> new PlanEntity(
                        personalPlanEntityInit(),
                        userEntity_t,
                        categories
                )
        ).limit(12).collect(Collectors.toList());
        // 지환이의 Plan을 16개 추가합니다.
        List<PlanEntity> planEntities_2 = Stream.generate(
                () -> new PlanEntity(
                        personalPlanEntityInit(),
                        userEntity_j,
                        categories
                )
        ).limit(16).collect(Collectors.toList());
        /**
         * 1. planEntityList 는 태현이의 plan 12개를 save 합니다.
         * 2. planEntityList_2 는 지환이의 plan 16개를 save 합니다.
         */
        List<PlanEntity> planEntityList = planRepository.saveAll(planEntities);
        List<PlanEntity> planEntityList_2 = planRepository.saveAll(planEntities_2);

        /**
         * When
         * 지환이 유저 엔티티에 해당하는 모든 개인일정을 찾습니다.
         */
        List<PlanEntity> allByUserId = planRepository.findAllPersonalPlanByUserEntity(userEntity_j);

        /**
         * Then
         * 지환이의 개인 일정이 총 16개가 맞나요? success!
         */
        assertEquals(16, allByUserId.size());
    }

    @Test @DisplayName("PersonalPlanService에 있는 getAllMyPersonalPlan 메서드를 사용할 수 있나요?")
    public void 개인일정_모두_찾기() {
        /**
         * Given
         * 1. personalPlanEntity 는 미리 메서드화 한 planEntity Builder를 추가시킵니다.
         * 2. userEntity_t 는 태현이의 유저 정보 save 메서드를 호출합니다.
         * 3. userEntity_j 는 지환이의 유저 정보 save 메서드를 호출합니다.
         */
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        UserEntity userEntity_t = userEntity_태현();
        UserEntity userEntity_j = userEntity_지환();
        List<String> categories = Collections.singletonList("지환이와 데이트");
        // BeforeEach 짱짱짱 Plan을 12개 추가합니다.
        List<PlanEntity> planEntities = Stream.generate(
                () -> new PlanEntity(
                        personalPlanEntityInit(),
                        beforeSavedUser,
                        categories
                )
        ).limit(12).collect(Collectors.toList());
        // 지환이의 Plan을 16개 추가합니다.
        List<PlanEntity> planEntities_2 = Stream.generate(
                () -> new PlanEntity(
                        personalPlanEntityInit(),
                        userEntity_j,
                        categories
                )
        ).limit(16).collect(Collectors.toList());
        /**
         * 1. planEntityList 는 태현이의 plan 12개를 save 합니다.
         * 2. planEntityList_2 는 지환이의 plan 16개를 save 합니다.
         */
        List<PlanEntity> planEntityList = planRepository.saveAll(planEntities);
        List<PlanEntity> planEntityList_2 = planRepository.saveAll(planEntities_2);

        /**
         * When
         * 짱짱짱 유저 엔티티에 해당하는 모든 개인일정을 찾습니다.
         */
        List<PlanEntity> allMyPersonalPlan = personalPlanService.getAllMyPersonalPlan();

        /**
         * Then
         * 짱짱짱 개인 일정이 총 12개가 맞나요? success!
         */
        assertEquals(12, allMyPersonalPlan.size());
    }

    @Test @DisplayName("PersonalPlanIdx를 넘겨줘서 해당 PlanEntity 가져오기")
    public void 이_일정을_찾아주세요(){
        /**
         * Given
         * 1. personalPlanEntity 는 미리 메서드화 한 planEntity Builder를 추가시킵니다.
         * 2. userEntity_t 는 태현이의 유저 정보 save 메서드를 호출합니다.
         * 3. userEntity_j 는 지환이의 유저 정보 save 메서드를 호출합니다.
         */
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        UserEntity userEntity_t = userEntity_태현();
        UserEntity userEntity_j = userEntity_지환();
        List<String> categories_j = Collections.singletonList("지환이와 데이트");
        List<String> categories_t = Collections.singletonList("태현이와 데이트");

        PlanEntity planEntity_j = new PlanEntity(
                personalPlanEntityInit(),
                userEntity_j,
                categories_j
        );

        PlanEntity planEntity_t = new PlanEntity(
                personalPlanEntityInit(),
                userEntity_t,
                categories_t
        );

        /**
         * 1. j_saved_plan 는 지환이의 일정을 save 합니다.
         * 2. t_saved_plan 는 태현이의 일정을 save 합니다.
         */
        PlanEntity j_saved_plan = planRepository.save(planEntity_j);
        PlanEntity t_saved_plan = planRepository.save(planEntity_t);

        /**
         * When
         * 지환이 유저 엔티티에 해당하는 모든 개인일정을 찾습니다.
         */
        PlanEntity getThisPlan = personalPlanService.getThisPersonalPlan(userEntity_j, planEntity_j.getPlanIdx());

        /**
         * Then
         * 가져온 Plan의 카테고리가 예상 값과 일치하나요?
         */
        assertEquals(categories_j, getThisPlan.getCategories());
    }

    @Test @DisplayName("일정 변경을 요청합니다.")
    public void 일정_변경() throws Exception {
        /**
         * Given
         * 1. personalPlanEntity 는 미리 메서드화 한 planEntity Builder를 추가시킵니다.
         * 2. userEntity_j 는 지환이의 유저 정보 세트 메서드를 호출합니다.
         */
        PersonalPlanEntity personalPlanEntity = personalPlanEntityInit();
        UserEntity userEntity_j = userEntity_지환();
        List<String> categories_j = Collections.singletonList("지환이와 데이트");

        PlanEntity planEntity_j = new PlanEntity(
                personalPlanEntityInit(),
                userEntity_j,
                categories_j
        );

        /**
         * j_saved_plan은 지환이의 일정을 save 합니다.
         */
        PlanEntity j_saved_plan = planRepository.save(planEntity_j);

        /**
         * 1. personalPlanUpdateDto 로 일정 변경사항을 세트합니다.
         * 2. service/updateThisPersonalPlan 에 기존 planIdx 와 변경사항Dto를 넘겨줍니다.
         */
        PersonalPlanUpdateDto personalPlanUpdateDto = PersonalPlanUpdateDto.builder()
                .planName("Gsm에서 지환이랑 놀기")
                .who("jihwan")
                .where("Gsm")
                .what("코딩")
                .when(Calendar.getInstance())
                .repeat(true)
                .build();

        personalPlanService.updateThisPersonalPlan(planEntity_j.getPlanIdx(), personalPlanUpdateDto);

        /**
         * Then
         * 변경사항 세트한 Dto의 PlanName으로 변경 됐습니까?
         */
        assertEquals(true, personalPlanRepository.findByPlanName("Gsm에서 지환이랑 놀기") != null);
    }
}