package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
import com.server.EZY.util.CurrentUserUtil;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ErrandServiceImplTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ErrandService errandService;

    MemberEntity savedMemberEntity;
    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
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

    @Test @DisplayName("심부름이 잘 저장되나요?")
    void 심부름_저장_조지기(){
        //Given
        ErrandSetDto errandSetDto = ErrandSetDto.builder()
                .location("수완스타벅스")
                .period(new Period(
                        LocalDateTime.of(2021, 7, 24, 1, 30),
                        LocalDateTime.of(2021, 7, 24, 1, 30)
                ))
                .planInfo(new PlanInfo("전지환이랑", "놀고오세요"))
                .recipient("@kim")
                .build();

        // 받을사람 유저 저장
        MemberEntity kimEntity = MemberEntity.builder()
                .username("@kim")
                .password("1234")
                .phoneNumber("010-1234-1234")
                .build();

        //When
        MemberEntity kimEntitySaved = memberRepository.save(kimEntity);
        ErrandEntity errandEntity = errandService.sendErrand(errandSetDto);

        //Then
        assertEquals(ResponseStatus.NOT_READ, errandEntity.getErrandStatusEntity().getResponseStatus());
        assertEquals(memberRepository.findByUsername("@kim").getMemberIdx(), errandEntity.getErrandStatusEntity().getRecipientIdx());
    }

}