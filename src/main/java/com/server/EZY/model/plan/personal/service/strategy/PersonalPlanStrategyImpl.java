package com.server.EZY.model.plan.personal.service.strategy;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalPlanStrategyImpl implements PersonalPlanStrategy{
    private final PersonalPlanRepository personalPlanRepository;

    @Override
    public PersonalPlanEntity singlePersonalPlanCheck(MemberEntity memberEntity, Long planIdx) {
        return personalPlanRepository.findThisPersonalPlanByMemberEntityAndPlanIdx(memberEntity, planIdx);
    }
}
