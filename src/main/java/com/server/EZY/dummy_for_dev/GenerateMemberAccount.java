package com.server.EZY.dummy_for_dev;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.dto.MemberDto;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * dev, test 환경에서 테스팅을 위한 회원 더미데이터 생성하는 클래스
 *
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Profile({"dev", "test"})
@Slf4j
@RequiredArgsConstructor
@Component
class GenerateMemberAccount {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Spring Boot 서버 실행 시점에 2021.11.7기준 server silo의 닉네임을 기준으로 회원을 생성후 AccessToken을 logging한다. <br>
     * <ul>
     *     <li>username : "siwony", "jyeonjyan", "qoxogus"</li>
     *     <li>password : "12345678"</li>
     * </ul>
     * @author 정시원
     */
    @PostConstruct
    private void createDummyMember(){
        MemberEntity siwony = createMemberOfSiwony();
        MemberEntity jyeonjyan = createMemberOfJyeonjyan();
        MemberEntity qoxogus = createMemberOfQoxogus();

        loggingAccessToken(siwony, jyeonjyan, qoxogus);
    }

    private void loggingAccessToken(MemberEntity...memberEntities){
        log.info("\n=============================================================================== Access Token =================================================================================");
        for (MemberEntity memberEntity: memberEntities)
            log.info("{}: \"{}\"", memberEntity.getUsername(), jwtTokenProvider.createToken(memberEntity.getUsername(), memberEntity.getRoles()));

        log.info("\n==============================================================================================================================================================================");
    }
    
    private MemberEntity createMemberOfSiwony(){
        MemberDto memberDto = MemberDto.builder()
                .username("@siwony")
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("01000000000")
                .fcmToken("cK_nm9tK3EdzgwOQXfjPbU:APA91bE-877ItvJsFelwTb23hntRort6v8fN-yGC8Mq1jzb8JEu3Qzi4oi7zJUKt6zigX02pjcQ84rwOQZjMngTdAkR6IKYygs-ZkGN94SNU6yY2MR8YqGvu2jLoPxQQ_f9pfQ8YdeZZ")
                .build();
        return memberRepository.save(memberDto.toEntity());
    }

    private MemberEntity createMemberOfJyeonjyan(){
        MemberDto memberDto = MemberDto.builder()
                .username("@jyeonjyan")
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("01000000001")
                .fcmToken("eQb5CygpsUahmPBRDnTc0N:APA91bFaOlt2nZDJKJpO8dZsjS8vSDCZKxZWYBWtNXYUiIiUxLPiGTLcXuyuVTW1uqOxu55Ay9z_1ss-D2uz2xP-C_R2-5yxyV2pqn88zYts4WSxS4pgWgdvFtBAG6nU__dSYH7WW8Qk")
                .build();
        return memberRepository.save(memberDto.toEntity());
    }

    private MemberEntity createMemberOfQoxogus(){
        MemberDto memberDto = MemberDto.builder()
                .username("@qoxogus")
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("01000000002")
                .fcmToken("fAp6e7Snyk_kg9ZxvTkt-a:APA91bEsOTGuuATRSKcHnwjqLL_aiT42BoLCuVJHrsW_JmvmfLqw8Ub2bZmUycR6qDyMbU2I41UScu9-kiv5bnI70wNRBXA1ku-IiEp5LiH_ZzGNBai7ZQqY5VGsb3s-BLu13iXEiISm")
                .build();
        return memberRepository.save(memberDto.toEntity());
    }
}
