package com.server.EZY.model.plan.personal.service.strategy;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;

public interface PersonalPlanStrategy {
    PersonalPlanDto.PersonalPlanDetails singlePersonalPlanCheck(MemberEntity memberEntity, Long planIdx);
}
