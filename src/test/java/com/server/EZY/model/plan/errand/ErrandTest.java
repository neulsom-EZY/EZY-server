package com.server.EZY.model.plan.errand;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
import com.server.EZY.model.plan.errand.repository.ErrandRepository;
import com.server.EZY.model.plan.errand.repository.ErrandStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ErrandTest {

    @Autowired MemberRepository memberRepository;
    @Autowired ErrandRepository errandRepository;
    @Autowired ErrandStatusRepository errandStatusRepository;

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
        ErrandStatusEntity errandStatusEntity = ErrandStatusEntity.builder()
                .responseStatus(ResponseStatus.NOT_READ)
                .senderIdx(memberSiwon.getMemberIdx())
                .recipientIdx(memberJihwan.getMemberIdx())
                .build();
        errandStatusEntity = errandStatusRepository.save(errandStatusEntity);
        Long errandStatusEntityIdx = errandStatusEntity.getErrandStatusIdx();

        PlanInfo planInfo = PlanInfo.builder()
                .title("PersonalPlanService CRUD 끝내기")
                .explanation("PersonalPlanService 끝내버려")
                .build();
        Period period = Period.builder()
                .startTime(LocalDateTime.of(20201, 7, 27, 17, 30))
                .endTime(LocalDateTime.of(20201, 7, 27, 20, 30))
                .build();

        ErrandEntity siwonErrand = ErrandEntity.builder()
                .memberEntity(memberSiwon)
                .tagEntity(null)
                .errandStatusEntity(errandStatusEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        ErrandEntity savedSiwonErrandEntity = errandRepository.save(siwonErrand);

        ErrandEntity jihwanErrand = ErrandEntity.builder()
                .memberEntity(memberJihwan)
                .tagEntity(null)
                .errandStatusEntity(errandStatusEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        ErrandEntity savedJihwanErrandEntity = errandRepository.save(jihwanErrand);
        // Then
        // errandStatusEntity == 시원의 errandStatusEntity == 지환의 errandStatusEntity
        assertEquals(errandStatusEntity, savedSiwonErrandEntity.getErrandStatusEntity()); // errandStatusEntity와 시원의 errandStatusEntity가 같으면
        assertEquals(savedSiwonErrandEntity.getErrandStatusEntity(), savedJihwanErrandEntity.getErrandStatusEntity()); //지환과 시원의 errandStatusEntity가 같으면 성공

        // planInfo == 시원의 planInfo == 지환의 planInfo
        assertEquals(planInfo, savedSiwonErrandEntity.getPlanInfo());
        assertEquals(savedSiwonErrandEntity.getPlanInfo(), jihwanErrand.getPlanInfo());

        // period == 시원의 period == 지환의 period
        assertEquals(period, savedSiwonErrandEntity.getPeriod());
        assertEquals(savedSiwonErrandEntity.getPeriod(), jihwanErrand.getPeriod());
    }

    @Test @DisplayName("심부름 삭제 테스트")
    void 심부름_삭제_테스트(){
        // Then
        ErrandStatusEntity errandStatusEntity = ErrandStatusEntity.builder()
                .responseStatus(ResponseStatus.NOT_READ)
                .senderIdx(memberSiwon.getMemberIdx())
                .recipientIdx(memberJihwan.getMemberIdx())
                .build();
        errandStatusEntity = errandStatusRepository.save(errandStatusEntity);

        PlanInfo planInfo = PlanInfo.builder()
                .title("PersonalPlanService CRUD 끝내기")
                .explanation("PersonalPlanService 끝내버려")
                .build();
        Period period = Period.builder()
                .startTime(LocalDateTime.of(20201, 7, 27, 17, 30))
                .endTime(LocalDateTime.of(20201, 7, 27, 20, 30))
                .build();

        ErrandEntity siwonErrand = ErrandEntity.builder()
                .memberEntity(memberSiwon)
                .tagEntity(null)
                .errandStatusEntity(errandStatusEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        ErrandEntity savedSiwonErrandEntity = errandRepository.save(siwonErrand);

        ErrandEntity jihwanErrand = ErrandEntity.builder()
                .memberEntity(memberJihwan)
                .tagEntity(null)
                .errandStatusEntity(errandStatusEntity)
                .planInfo(planInfo)
                .period(period)
                .build();
        ErrandEntity savedJihwanErrandEntity = errandRepository.save(jihwanErrand);

        // When
        errandRepository.deleteById(siwonErrand.getPlanIdx());
        errandRepository.deleteById(jihwanErrand.getPlanIdx());
        errandStatusRepository.deleteById(errandStatusEntity.getErrandStatusIdx());

        // Then
        assertFalse(errandStatusRepository.existsById(1L));
        assertFalse(errandRepository.existsById(1L));
        assertFalse(errandRepository.existsById(2L));
    }

}