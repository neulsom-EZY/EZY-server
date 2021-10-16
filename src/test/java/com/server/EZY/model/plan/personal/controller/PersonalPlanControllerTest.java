package com.server.EZY.model.plan.personal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.testConfig.AbstractControllerTest;
import com.server.EZY.util.CurrentUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@Transactional
class PersonalPlanControllerTest extends AbstractControllerTest {
    @Autowired
    private PersonalPlanController personalPlanController;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper mapper;

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

    @Override
    protected Object controller() {
        return personalPlanController;
    }

    @Test
    public void savePersonalPlan() throws Exception {
        PersonalPlanSetDto personalPlanSetDto = PersonalPlanSetDto.builder()
                .planInfo(new PlanInfo("와우껌", "좋아요", "광주광역시"))
                .period(new Period(
                                LocalDateTime.of(2021, 7, 24, 1, 30),
                                LocalDateTime.of(2021, 7, 24, 1, 30)
                        )
                )
                .tagIdx(1L)
                .repetition(false)
                .build();

        mvc.perform(
                post("/v1/plan/personal")
                .content(mapper.writeValueAsString(personalPlanSetDto))
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    public void getAllPersonalPlan() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startDate", "2020-10-22");
        params.add("endDate", "2020-12-12");

        mvc.perform(
                get("/v1/plan/personal")
                .params(params)
        );
    }

    @Test
    public void getThisPersonalPlan() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("planIdx", "1L");

        mvc.perform(
                get("/v1/plan/personal")
                .params(params)
        );
    }

    @Test @Disabled
    public void updateThisPersonalPlan() throws Exception{
        PersonalPlanSetDto personalPlanSetDto = PersonalPlanSetDto.builder()
                .planInfo(new PlanInfo("이걸", "변경할거야", "광주광역시"))
                .period(new Period(
                                LocalDateTime.of(2021, 7, 24, 1, 30),
                                LocalDateTime.of(2021, 7, 24, 1, 30)
                        )
                )
                .tagIdx(1L)
                .repetition(false)
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("planIdx", "1L");

        mvc.perform(
                put("/v1/plan/personal/{planIdx}", 1L)
                        .content(mapper.writeValueAsString(personalPlanSetDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test @Disabled
    public void deleteThisPersonalPlan() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("planIdx", "1");

        mvc.perform(
                delete("/v1/plan/personal/{planIdx}", 1L)
        );
    }
}