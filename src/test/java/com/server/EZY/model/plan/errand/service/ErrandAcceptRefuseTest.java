package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
public class ErrandAcceptRefuseTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private  ErrandService errandService;

    // 전지환의 token
    private final String SENDER_FCM_TOKEN = "eQb5CygpsUahmPBRDnTc0N:APA91bFaOlt2nZDJKJpO8dZsjS8vSDCZKxZWYBWtNXYUiIiUxLPiGTLcXuyuVTW1uqOxu55Ay9z_1ss-D2uz2xP-C_R2-5yxyV2pqn88zYts4WSxS4pgWgdvFtBAG6nU__dSYH7WW8Qk";

    // 정시원의 token
    private final String RECIPIENT_FCM_TOKEN = "cK_nm9tK3EdzgwOQXfjPbU:APA91bE-877ItvJsFelwTb23hntRort6v8fN-yGC8Mq1jzb8JEu3Qzi4oi7zJUKt6zigX02pjcQ84rwOQZjMngTdAkR6IKYygs-ZkGN94SNU6yY2MR8YqGvu2jLoPxQQ_f9pfQ8YdeZZ";

    private final String SENDER_USERNAME = "@sender";
    private final String RECIPIENT_USERNAME = "@recipient";

    MemberEntity makeMember(String username, String fcmToken){
        MemberEntity memberEntity = MemberEntity.builder()
                .username(username)
                .password("010" + RandomStringUtils.randomAlphabetic(8))
                .phoneNumber(RandomStringUtils.randomNumeric(8))
                .fcmToken(fcmToken)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
        return memberRepository.save(memberEntity);
    }

    void signInMember(MemberEntity memberEntity){
        // when login session 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                memberEntity.getUsername(),
                memberEntity.getPassword(),
                memberEntity.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        System.out.println("=================================");
        System.out.println(context);

        //then
        String currentUserNickname = CurrentUserUtil.getCurrentUsername();
        assertEquals(memberEntity.getUsername(), currentUserNickname);
    }

    ErrandEntity sendErrand(MemberEntity sender) throws Exception {
        signInMember(sender); // 발신자 로그인
        ErrandSetDto errandSetDto = ErrandSetDto.builder()
                .location(RandomStringUtils.randomAlphabetic(6))
                .period(new Period(
                        LocalDateTime.of(2021, 7, 24, 1, 30),
                        LocalDateTime.of(2021, 7, 24, 1, 30)
                ))
                .planInfo(new PlanInfo(RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(6), RandomStringUtils.randomAlphabetic(6)))
                .recipient(RECIPIENT_USERNAME)
                .build();

        return errandService.sendErrand(errandSetDto);
    }

    @Test @DisplayName("심부름 수락 검증")
    void 심부름_수락_검증() throws Exception {
        log.info("========= Given =========");
        // 발신자, 수신자 생성
        MemberEntity sender = makeMember(SENDER_USERNAME, SENDER_FCM_TOKEN);
        MemberEntity recipient = makeMember(RECIPIENT_USERNAME, RECIPIENT_FCM_TOKEN);

        // 발신자가 심부름 보냄
        ErrandEntity senderErrandEntity = sendErrand(sender);
        ErrandStatusEntity senderErrandStatusEntity = senderErrandEntity.getErrandStatusEntity();

        log.info("========= When =========");
        //로그인 후 sender의 심부름을 수락함
        signInMember(recipient);
        ErrandEntity recipientErrandEntity = errandService.acceptErrand(senderErrandEntity.getPlanIdx());
        ErrandStatusEntity recipientErrandStatusEntity = recipientErrandEntity.getErrandStatusEntity();

        log.info("========= Then =========");
        // 발신자, 수신자의 심부름 정보가 같은지 검증
        assertEquals(senderErrandEntity.getPlanInfo(), recipientErrandEntity.getPlanInfo());
        assertEquals(senderErrandEntity.getPeriod(), recipientErrandEntity.getPeriod());
        assertEquals(senderErrandEntity.getLocation(), recipientErrandEntity.getLocation());

        assertEquals(senderErrandStatusEntity, recipientErrandStatusEntity);
    }
}
