package com.server.EZY.model.plan.personal.service.strategy;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalPlanStrategyImpl implements PersonalPlanStrategy{
    private final PersonalPlanQueryRepository personalPlanQueryRepository;

    @Override
    public PersonalPlanDto.PersonalPlanDetails singlePersonalPlanCheck(MemberEntity memberEntity, Long planIdx) {
        return personalPlanQueryRepository.getPersonalPlanDetailsByPersonalPlanIdx(memberEntity, planIdx);
    }
}
