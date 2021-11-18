package com.server.EZY.model.plan.personal.service.strategy;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version 1
 * @since 1
 * @author 전지환
 */
@Service
@RequiredArgsConstructor
public class PersonalPlanStrategyImpl implements PersonalPlanStrategy{
    private final PersonalPlanRepository personalPlanRepository;

    /**
     * 개인일정 단건을 personalPlanEntity로 반환하는 전략 메소드.
     *
     * @param memberEntity
     * @param planIdx
     * @return PersonalPlanEntity
     */
    @Override
    public PersonalPlanEntity singlePersonalPlanCheck(MemberEntity memberEntity, Long planIdx) {
        return personalPlanRepository.findThisPersonalPlanByMemberEntityAndPlanIdx(memberEntity, planIdx);
    }
}
