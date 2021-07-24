package com.server.EZY.model.plan.personal;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.repository.TagRepository;
import com.server.EZY.testConfig.QueryDslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest @Import(QueryDslTestConfig.class)
class PersonalPlanEntityTest {

    @Autowired PersonalPlanRepository personalPlanRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TagRepository tagRepository;

    PersonalPlanEntity personalPlanInit(){
        MemberEntity memberEntity = MemberEntity.builder()
                .username("JsonWebTok")
                .password("JsonWebTok")
                .phoneNumber("01012345678")
                .roles(Collections.singletonList(Role.ROLE_ADMIN))
                .build();
        MemberEntity savedMemberEntity = memberRepository.getById(memberRepository.save(memberEntity).getUserIdx());

        TagEntity tagEntity = TagEntity.builder()
                .memberEntity(savedMemberEntity)
                .tag("공부")
                .build();
        TagEntity savedTagEntity = tagRepository.getById(tagRepository.save(tagEntity).getTagIdx());

        PlanInfo planInfo = PlanInfo.builder()
                .title("카페가서 공부하기")
                .explanation("카페가서 모던 자바 책 스터디!")
                .build();

        Period period = Period.builder()
                .startTime(LocalDateTime.of(2021, 7, 24, 1, 30))
                .endTime(LocalDateTime.of(2021, 7, 24, 6, 30))
                .build();

        Boolean repetition = true;

        PersonalPlanEntity personalPlanEntity = new PersonalPlanEntity(savedMemberEntity, tagEntity, planInfo, period, repetition);
        return personalPlanRepository.save(personalPlanEntity);
    }

    @Test @DisplayName("PersonalPlanEntity 생성, 조회, 삭제 테스트")
    void PersonalPlanEntity_생성_조회_삭제_테스트(){
        // Given
        MemberEntity memberEntity = MemberEntity.builder()
                .username("JsonWebTok")
                .password("JsonWebTok")
                .phoneNumber("01012345678")
                .roles(Collections.singletonList(Role.ROLE_ADMIN))
                .build();
        MemberEntity savedMemberEntity = memberRepository.getById(memberRepository.save(memberEntity).getUserIdx());

        TagEntity tagEntity = TagEntity.builder()
                .memberEntity(savedMemberEntity)
                .tag("공부")
                .build();
        TagEntity savedTagEntity = tagRepository.getById(tagRepository.save(tagEntity).getTagIdx());

        PlanInfo planInfo = PlanInfo.builder()
                .title("카페가서 공부하기")
                .explanation("카페가서 모던 자바 책 스터디!")
                .build();

        Period period = Period.builder()
                .startTime(LocalDateTime.of(2021, 7, 24, 1, 30))
                .endTime(LocalDateTime.of(2021, 7, 24, 6, 30))
                .build();

        Boolean repetition = true;

        PersonalPlanEntity personalPlanEntity = new PersonalPlanEntity(savedMemberEntity, tagEntity, planInfo, period, repetition);

        // When
        //개인일정 저장
        PersonalPlanEntity savedPersonalPlanEntity = personalPlanRepository.getById(personalPlanRepository.save(personalPlanEntity).getPlanIdx());

        //저장한 개인일정 삭제
        personalPlanRepository.delete(savedPersonalPlanEntity);

        // Then
        assertEquals(savedMemberEntity, savedPersonalPlanEntity.getMemberEntity());
        assertEquals(savedTagEntity, savedPersonalPlanEntity.getTagEntity());
        assertEquals(planInfo, savedPersonalPlanEntity.getPlanInfo());
        assertEquals(period, savedPersonalPlanEntity.getPeriod());
        assertEquals(repetition, savedPersonalPlanEntity.getRepetition());

        assertThrows
            (
                JpaObjectRetrievalFailureException.class,
                () -> personalPlanRepository.getById(savedPersonalPlanEntity.getPlanIdx()),
                    "생성한 개인일정이 삭제되었습니다."
            );
    }

    @Test @DisplayName("PersonalPlanEntity 업데이트 테스트")
    void PersonalPlanEntity_업데이트_테스트() throws Exception {
        // Given
        PersonalPlanEntity savedPersonalPlanEntity = personalPlanInit();

        // 개인일정에서 수정할 TagEntity
        TagEntity tagEntity = TagEntity.builder()
                .memberEntity(savedPersonalPlanEntity.getMemberEntity())
                .tag("놀기")
                .build();
        TagEntity savedTagEntity = tagRepository.getById(tagRepository.save(tagEntity).getTagIdx());

        // 개인일정에서 수정할 planInfo
        PlanInfo planInfo = PlanInfo.builder()
                .title("PC방가서 배그하기")
                .explanation("오늘은 치킨이닭")
                .build();

        // 개인일정에서 수정할 기간
        Period period = Period.builder()
                .startTime(LocalDateTime.of(2021, 7, 25, 1, 30))
                .endTime(LocalDateTime.of(2021, 7, 25, 6, 30))
                .build();

        // 개인일정에서 수정할 반복
        boolean repetition = false;


        // When
        savedPersonalPlanEntity.updatePersonalPlanEntity(savedPersonalPlanEntity.getMemberEntity(), tagEntity, planInfo, period, repetition);

        // Then
        assertEquals(savedTagEntity, savedPersonalPlanEntity.getTagEntity(), savedPersonalPlanEntity.getTagEntity().getTag());
        assertEquals(planInfo, savedPersonalPlanEntity.getPlanInfo(), "title = " + savedPersonalPlanEntity.getPlanInfo().getTitle() + ", explanation = " + savedPersonalPlanEntity.getPlanInfo().getExplanation());
        assertEquals(period, savedPersonalPlanEntity.getPeriod(), "startTime = " + savedPersonalPlanEntity.getPeriod().getStartTime().toString() + ", endTime = " + savedPersonalPlanEntity.getPeriod().getEndTime().toString());
        assertEquals(repetition, savedPersonalPlanEntity.getRepetition());
    }

}