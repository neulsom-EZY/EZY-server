package com.server.EZY.model.plan.errand.service;

import com.server.EZY.exception.response.CustomException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.member.service.MemberService;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandDetailEntity;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandResponseDto;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandStatus;
import com.server.EZY.model.plan.errand.repository.errand.ErrandRepository;
import com.server.EZY.model.plan.errand.repository.errand_detail.ErrandDetailRepository;
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

    @Autowired
    private ErrandDetailRepository errandDetailRepository;
    @Autowired
    private ErrandRepository errandRepository;

    String jihwanFcmToken = "eQb5CygpsUahmPBRDnTc0N:APA91bFaOlt2nZDJKJpO8dZsjS8vSDCZKxZWYBWtNXYUiIiUxLPiGTLcXuyuVTW1uqOxu55Ay9z_1ss-D2uz2xP-C_R2-5yxyV2pqn88zYts4WSxS4pgWgdvFtBAG6nU__dSYH7WW8Qk";
    String youjinFcmToken = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";
    String siwonyFcmToken = "cK_nm9tK3EdzgwOQXfjPbU:APA91bE-877ItvJsFelwTb23hntRort6v8fN-yGC8Mq1jzb8JEu3Qzi4oi7zJUKt6zigX02pjcQ84rwOQZjMngTdAkR6IKYygs-ZkGN94SNU6yY2MR8YqGvu2jLoPxQQ_f9pfQ8YdeZZ";
    String teahyeonFcmToken = "fAp6e7Snyk_kg9ZxvTkt-a:APA91bEsOTGuuATRSKcHnwjqLL_aiT42BoLCuVJHrsW_JmvmfLqw8Ub2bZmUycR6qDyMbU2I41UScu9-kiv5bnI70wNRBXA1ku-IiEp5LiH_ZzGNBai7ZQqY5VGsb3s-BLu13iXEiISm";
    String testingFcmToken = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";

    public MemberEntity myMemberEntity;

    @BeforeEach
    @DisplayName("????????? ???????????? ????????? ???????????? ?????????")
    void GetUserEntity(){
        //Given
        MemberDto memberDto = MemberDto.builder()
                .username("@jyeonjyan")
                .password("1234")
                .phoneNumber(RandomStringUtils.randomNumeric(11))
                .fcmToken(testingFcmToken)
                .build();

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        myMemberEntity = memberService.signup(memberDto);
        System.out.println("======== saved =========");

        // when login session ??????
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

    /**
     * ?????????(??????)??? ???????????????.
     *
     * @param sender
     * @param recipient
     * @author ?????????
     */
    List<ErrandEntity> errandGenerate(MemberEntity sender, MemberEntity recipient){
        ErrandDetailEntity errandDetailEntity = ErrandDetailEntity.builder()
                .errandStatus(ErrandStatus.NONE)
                .senderIdx(sender.getMemberIdx())
                .recipientIdx(recipient.getMemberIdx())
                .build();
        errandDetailRepository.save(errandDetailEntity);

        ErrandEntity senderErrandEntity = ErrandEntity.builder()
                .memberEntity(sender)
                .period(
                        Period.builder()
                                .startDateTime(LocalDateTime.of(2021, 11, 11, 1, 0))
                                .endDateTime(LocalDateTime.of(2021, 11, 13, 1, 0))
                                .build()
                )
                .planInfo(PlanInfo.builder()
                        .title(RandomStringUtils.randomAlphabetic(10))
                        .explanation(RandomStringUtils.randomAlphabetic(30))
                        .location(RandomStringUtils.randomAlphabetic(30))
                        .build()
                )
                .errandDetailEntity(errandDetailEntity)
                .build();

        ErrandEntity recipientErrandEntity = senderErrandEntity.cloneToMemberEntity(recipient);

        return errandRepository.saveAll(List.of(senderErrandEntity, recipientErrandEntity));
    }

    @Test @DisplayName("???????????? ??? ????????????????") @Disabled
    void ?????????_??????_?????????() throws Exception {
        log.info("==========Given ????????? ??????==========");
        ErrandSetDto errandSetDto = ErrandSetDto.builder()
                .period(new Period(
                        LocalDateTime.of(2021, 7, 24, 1, 30),
                        LocalDateTime.of(2021, 7, 24, 1, 30)
                ))
                .planInfo(new PlanInfo("???????????????", "???????????????", "??????"))
                .recipient("@myName")
                .build();

        log.info("===========Given ???????????? ?????? ??????============");
        MemberEntity kimEntity = MemberEntity.builder()
                .username("@"+RandomStringUtils.randomAlphabetic(5))
                .password("1234")
                .phoneNumber(RandomStringUtils.randomNumeric(11))
                .fcmToken("testingFcmToken1")
                .build();


        log.info("========= When ???????????? ?????? ?????? ==========");
        MemberEntity kimEntitySaved = memberRepository.save(kimEntity);
        log.info("========= When ????????? ?????? ==========");
        ErrandEntity errandEntity = errandService.sendErrand(errandSetDto);

        //Then
        assertEquals(ErrandStatus.NONE, errandEntity.getErrandDetailEntity().getErrandStatus());
        assertEquals(memberRepository.findByUsername(kimEntity.getUsername()).getMemberIdx(), errandEntity.getErrandDetailEntity().getRecipientIdx());
    }

    /**
     * ???????????? ?????? ???????????????.
     *
     * @author ?????????
     */
    @Test @DisplayName("????????? ?????? ????????????")
    void ??????_?????????_??????_????????????(){
        log.info("========= Given: ????????????_1 memberEntity ?????? =========");
        MemberEntity recipient = MemberEntity.builder()
                .username("@"+RandomStringUtils.randomAlphabetic(4))
                .password("1234")
                .phoneNumber(RandomStringUtils.randomNumeric(11))
                .fcmToken("testingFcmToken2")
                .build();
        MemberEntity recipientEntity = memberRepository.save(recipient);

        log.info("=========== Given: ???????????????_2 memberEntity ?????? =========");
        MemberEntity sender_2 = MemberEntity.builder()
                .username("@"+RandomStringUtils.randomAlphabetic(4))
                .password("1234")
                .phoneNumber(RandomStringUtils.randomNumeric(11))
                .fcmToken("testingFcmToken3")
                .build();
        MemberEntity senderEntity_2 = memberRepository.save(sender_2);

        log.info("=========== Given ????????? ?????? 3??? ============");
        List<ErrandEntity> errandEntities = errandGenerate(myMemberEntity, recipientEntity);
        errandGenerate(myMemberEntity, recipientEntity);
        errandGenerate(senderEntity_2, recipientEntity);

        log.info("========== when ?????? ????????? ?????? ============");
        List<ErrandResponseDto.ErrandPreview> allMyErrands = errandService.findAllMyErrands();

        log.info("============ then ?????? ????????? ?????? ????????? ????????? ============");
        Integer integer = allMyErrands.size();
        assertEquals(integer, 2);
    }

    @Test @DisplayName("????????? ?????? ?????? ????????????")
    void ?????????_??????_????????????() throws Exception {
        log.info("========= Given: ????????????_1 memberEntity ?????? =========");
        MemberEntity recipient = MemberEntity.builder()
                .username("@"+RandomStringUtils.randomAlphabetic(4))
                .password("1234")
                .phoneNumber(RandomStringUtils.randomNumeric(11))
                .fcmToken("testingFcmToken2")
                .build();
        MemberEntity recipientEntity = memberRepository.save(recipient);

        log.info("=========== Given: ???????????????_2 memberEntity ?????? =========");
        MemberEntity sender_2 = MemberEntity.builder()
                .username("@"+RandomStringUtils.randomAlphabetic(4))
                .password("1234")
                .phoneNumber(RandomStringUtils.randomNumeric(11))
                .fcmToken("testingFcmToken3")
                .build();
        MemberEntity senderEntity_2 = memberRepository.save(sender_2);

        log.info("===== ???(???????????????) ????????? 1??? ?????? =====");
        List<ErrandEntity> errandEntities = errandGenerate(myMemberEntity, recipientEntity);

        log.info("======== then ?????? ????????? ?????????????????? ========");
        // ????????? ????????? ????????? ????????? ??? ??????
        if (errandEntities.get(0) == null){
            throw new Exception("?????? ????????? ????????? ???????????? ???????????? ????????? ????????????.");
        }
        // ????????????
        ErrandResponseDto.ErrandDetails errandDetails = errandService.findErrandDetails(errandEntities.get(0).getPlanIdx());
        assertNotNull(errandDetails);
    }
}