package com.server.EZY.model.plan.errand;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enum_type.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.errand.enum_type.ErrandStatus;
import com.server.EZY.model.plan.errand.repository.errand.ErrandRepository;
import com.server.EZY.model.plan.errand.repository.errand_detail.ErrandDetailRepository;
import com.server.EZY.testConfig.QueryDslTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Import(QueryDslTestConfig.class)
class ErrandTest {

    @Autowired MemberRepository memberRepository;
    @Autowired ErrandRepository errandRepository;
    @Autowired
    ErrandDetailRepository errandDetailRepository;

    MemberEntity memberSiwon;
    MemberEntity memberJihwan;

    @BeforeEach
    void init(){
        memberSiwon = MemberEntity.builder()
                .password("1234")
                .username("시원")
                .phoneNumber("01011111111")
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
        memberSiwon = memberRepository.save(memberSiwon);

        memberJihwan = MemberEntity.builder()
                .password("1234")
                .username("지환")
                .phoneNumber("01012341234")
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
        memberJihwan = memberRepository.save(memberJihwan);
    }

    @Test @DisplayName("심부름 생성, 조회 테스트")
    void 심부름_저장_조회_테스트(){
        /** given
         * 시원과 지환의 member를 만들고 저장한다. -> init() 매서드 참고
         * 심부름의 발신자는 시원, 수신자는 지환
         */

        /** When
         * errandStatus를 만들어 시원과 지환의 각각생성된 Errand테이블과 연관관계를 맻고 저장한다.
         */
        ErrandDetailEntity errandDetailEntity = ErrandDetailEntity.builder()
                .errandStatus(ErrandStatus.NONE)
                .senderIdx(memberSiwon.getMemberIdx())
                .recipientIdx(memberJihwan.getMemberIdx())
                .build();
        errandDetailEntity = errandDetailRepository.save(errandDetailEntity);
        Long ErrandDetailEntityIdx = errandDetailEntity.getErrandDetailIdx();

        PlanInfo planInfo = PlanInfo.builder()
                .title("PersonalPlanService CRUD 끝내기")
                .explanation("PersonalPlanService 끝내버려")
                .build();
        Period period = Period.builder()
                .startDateTime(LocalDateTime.of(20201, 7, 27, 17, 30))
                .endDateTime(LocalDateTime.of(20201, 7, 27, 20, 30))
                .build();

        ErrandEntity siwonErrand = ErrandEntity.builder()
                .memberEntity(memberSiwon)
                .tagEntity(null)
                .errandDetailEntity(errandDetailEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        ErrandEntity savedSiwonErrandEntity = errandRepository.save(siwonErrand);

        ErrandEntity jihwanErrand = ErrandEntity.builder()
                .memberEntity(memberJihwan)
                .tagEntity(null)
                .errandDetailEntity(errandDetailEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        ErrandEntity savedJihwanErrandEntity = errandRepository.save(jihwanErrand);
        // Then
        // ErrandDetailEntity == 시원의 ErrandDetailEntity == 지환의 ErrandDetailEntity
        assertEquals(errandDetailEntity, savedSiwonErrandEntity.getErrandDetailEntity()); // ErrandDetailEntity와 시원의 ErrandDetailEntity가 같으면
        assertEquals(savedSiwonErrandEntity.getErrandDetailEntity(), savedJihwanErrandEntity.getErrandDetailEntity()); //지환과 시원의 ErrandDetailEntity가 같으면 성공

        // planInfo == 시원의 planInfo == 지환의 planInfo
        assertEquals(planInfo, savedSiwonErrandEntity.getPlanInfo());
        assertEquals(savedSiwonErrandEntity.getPlanInfo(), jihwanErrand.getPlanInfo());

        // period == 시원의 period == 지환의 period
        assertEquals(period, savedSiwonErrandEntity.getPeriod());
        assertEquals(savedSiwonErrandEntity.getPeriod(), jihwanErrand.getPeriod());

        assertNull(savedSiwonErrandEntity.getPlanInfo().getLocation()); // location를 저장안했기 때문에 null
        assertEquals(savedSiwonErrandEntity.getPlanInfo().getLocation(), savedJihwanErrandEntity.getPlanInfo().getLocation());
    }

    @Test @DisplayName("심부름 삭제 테스트")
    void 심부름_삭제_테스트(){
        // Then
        ErrandDetailEntity errandDetailEntity = ErrandDetailEntity.builder()
                .errandStatus(ErrandStatus.NONE)
                .senderIdx(memberSiwon.getMemberIdx())
                .recipientIdx(memberJihwan.getMemberIdx())
                .build();
        errandDetailEntity = errandDetailRepository.save(errandDetailEntity);

        PlanInfo planInfo = PlanInfo.builder()
                .title("PersonalPlanService CRUD 끝내기")
                .explanation("PersonalPlanService 끝내버려")
                .build();
        Period period = Period.builder()
                .startDateTime(LocalDateTime.of(20201, 7, 27, 17, 30))
                .endDateTime(LocalDateTime.of(20201, 7, 27, 20, 30))
                .build();

        ErrandEntity siwonErrand = ErrandEntity.builder()
                .memberEntity(memberSiwon)
                .tagEntity(null)
                .errandDetailEntity(errandDetailEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        errandRepository.save(siwonErrand);

        ErrandEntity jihwanErrand = ErrandEntity.builder()
                .memberEntity(memberJihwan)
                .tagEntity(null)
                .errandDetailEntity(errandDetailEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        errandRepository.save(jihwanErrand);

        // When
        errandRepository.deleteById(siwonErrand.getPlanIdx());
        errandRepository.deleteById(jihwanErrand.getPlanIdx());

        // Then
        assertThrows(
                NoSuchElementException.class,
                () -> errandDetailRepository.findById(1L).get()
        );
        assertThrows(
                NoSuchElementException.class,
                () -> errandRepository.findById(1L).get()
        );
        assertThrows(
                NoSuchElementException.class,
                () -> errandRepository.findById(2L).get()
        );
    }

}