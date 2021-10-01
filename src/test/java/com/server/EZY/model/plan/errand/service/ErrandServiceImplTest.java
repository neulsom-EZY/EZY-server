package com.server.EZY.model.plan.errand.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.service.FirebaseMessagingService;
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
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private ErrandService errandService;

    String jihwanFcmToken = "eQb5CygpsUahmPBRDnTc0N:APA91bFaOlt2nZDJKJpO8dZsjS8vSDCZKxZWYBWtNXYUiIiUxLPiGTLcXuyuVTW1uqOxu55Ay9z_1ss-D2uz2xP-C_R2-5yxyV2pqn88zYts4WSxS4pgWgdvFtBAG6nU__dSYH7WW8Qk";
    String youjinFcmToken = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";

    MemberEntity savedMemberEntity;
    @BeforeEach
    @DisplayName("로그인 되어있는 유저를 확인하는 테스트")
    void GetUserEntity(){
        //Given
        MemberDto memberDto = MemberDto.builder()
                .username("전지환")
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
        assertEquals("전지환", currentUserNickname);
    }

    @Test @DisplayName("심부름이 잘 저장되나요?")
    void 심부름_저장_조지기() throws FirebaseMessagingException {
        //Given
        ErrandSetDto errandSetDto = ErrandSetDto.builder()
                .location("수완스타벅스")
                .period(new Period(
                        LocalDateTime.of(2021, 7, 24, 1, 30),
                        LocalDateTime.of(2021, 7, 24, 1, 30)
                ))
                .planInfo(new PlanInfo("전지환이랑", "놀고오세요", "광주"))
                .recipient("@kim")
                .build();

        // 받을사람 유저 저장
        MemberEntity kimEntity = MemberEntity.builder()
                .username("@kim")
                .password("1234")
                .phoneNumber("01023212312")
                .build();

        //When
        MemberEntity kimEntitySaved = memberRepository.save(kimEntity);
        ErrandEntity errandEntity = errandService.sendErrand(errandSetDto);

        //Then
        assertEquals(ErrandResponseStatus.NOT_READ, errandEntity.getErrandStatusEntity().getErrandResponseStatus());
        assertEquals(memberRepository.findByUsername("@kim").getMemberIdx(), errandEntity.getErrandStatusEntity().getRecipientIdx());
    }

    @Test @DisplayName("심부름 상태 관련 푸시 알림이 잘 동작하나요?")
    void 심부름_Res_Status_문구확인() throws FirebaseMessagingException {
        //Given
        FcmMessage.FcmRequest messageAboutErrand = errandService.createFcmMessageAboutErrand("jyeonjyan", null, null, ErrandResponseStatus.CANCEL);
        //When
        firebaseMessagingService.sendToToken(messageAboutErrand, youjinFcmToken);
    }
}