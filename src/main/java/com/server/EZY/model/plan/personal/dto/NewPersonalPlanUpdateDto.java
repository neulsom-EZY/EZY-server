package com.server.EZY.model.plan.personal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.EZY.model.plan.Period;
import com.server.EZY.model.plan.PlanInfo;
import com.server.EZY.model.plan.personal.NewPersonalPlanEntity;
import com.server.EZY.model.member.MemberEntity;
import lombok.*;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
public class NewPersonalPlanUpdateDto {

    @JsonIgnore
    private MemberEntity memberEntity;

    private PlanInfo planInfo;
    private Period period;
    private boolean repetition;

    public NewPersonalPlanEntity toEntity(){
        return new NewPersonalPlanEntity(memberEntity, planInfo, period, repetition);
    }

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
}
