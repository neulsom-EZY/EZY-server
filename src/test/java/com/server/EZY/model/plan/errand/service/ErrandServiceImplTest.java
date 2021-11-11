package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.member.service.MemberService;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandStatus;
import com.server.EZY.notification.service.FirebaseMessagingService;
import com.server.EZY.util.CurrentUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ErrandServiceImplTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private ErrandService errandService;

    String jihwanFcmToken = "eQb5CygpsUahmPBRDnTc0N:APA91bFaOlt2nZDJKJpO8dZsjS8vSDCZKxZWYBWtNXYUiIiUxLPiGTLcXuyuVTW1uqOxu55Ay9z_1ss-D2uz2xP-C_R2-5yxyV2pqn88zYts4WSxS4pgWgdvFtBAG6nU__dSYH7WW8Qk";
    String youjinFcmToken = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";
    String siwonyFcmToken = "cK_nm9tK3EdzgwOQXfjPbU:APA91bE-877ItvJsFelwTb23hntRort6v8fN-yGC8Mq1jzb8JEu3Qzi4oi7zJUKt6zigX02pjcQ84rwOQZjMngTdAkR6IKYygs-ZkGN94SNU6yY2MR8YqGvu2jLoPxQQ_f9pfQ8YdeZZ";
    String teahyeonFcmToken = "fAp6e7Snyk_kg9ZxvTkt-a:APA91bEsOTGuuATRSKcHnwjqLL_aiT42BoLCuVJHrsW_JmvmfLqw8Ub2bZmUycR6qDyMbU2I41UScu9-kiv5bnI70wNRBXA1ku-IiEp5LiH_ZzGNBai7ZQqY5VGsb3s-BLu13iXEiISm";
    String testingFcmToken = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";

    MemberEntity savedMemberEntity;
    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void GetUserEntity(){
        //Given
        MemberDto memberDto = MemberDto.builder()
                .username("@jyeonjyan")
                .password("1234")
                .phoneNumber("01022222222")
                .fcmToken(testingFcmToken)
                .build();

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        savedMemberEntity = memberService.signup(memberDto);
        System.out.println("======== saved =========");

        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberDto.getUsername(),
                memberDto.getPassword(),
                List.of(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name()))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUsername();
        assertEquals("@jyeonjyan", currentUserNickname);
    }

    @Test @DisplayName("심부름이 잘 저장되나요?") @Disabled
    void 심부름_저장_조지기() throws Exception {
        log.info("==========Given 심부름 세팅==========");
        ErrandSetDto errandSetDto = ErrandSetDto.builder()
                .location("수완스타벅스")
                .period(new Period(
                        LocalDateTime.of(2021, 7, 24, 1, 30),
                        LocalDateTime.of(2021, 7, 24, 1, 30)
                ))
                .planInfo(new PlanInfo("전지환이랑", "놀고오세요", "광주"))
                .recipient("@myName")
                .build();

        log.info("===========Given 받는사람 회원 세팅============");
        MemberEntity kimEntity = MemberEntity.builder()
                .username("@"+RandomStringUtils.randomAlphabetic(5))
                .password("1234")
                .phoneNumber(RandomStringUtils.randomNumeric(11))
                .fcmToken(testingFcmToken)
                .build();

        log.info("========= When 받는사람 회원 저장 ==========");
        MemberEntity kimEntitySaved = memberRepository.save(kimEntity);
        log.info("========= When 심부름 저장 ==========");
        ErrandEntity errandEntity = errandService.sendErrand(errandSetDto);

        //Then
        assertEquals(ErrandStatus.NONE, errandEntity.getErrandDetailEntity().getErrandStatus());
        assertEquals(memberRepository.findByUsername(kimEntity.getUsername()).getMemberIdx(), errandEntity.getErrandDetailEntity().getRecipientIdx());
    }
}