package com.server.EZY.model.plan.personal.dto;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class PersonalPlanSetDto {
    @NotNull
    private PlanInfo planInfo;
    @NotNull
    private Period period;
    private TagEntity tag;
    @NotNull
    private Boolean repetition;

    public PersonalPlanEntity saveToEntity(MemberEntity memberEntity){
        return PersonalPlanEntity.builder()
                .memberEntity(memberEntity)
                .tagEntity(this.tag)
                .planInfo(this.planInfo)
                .period(this.period)
                .repetition(this.repetition)
                .build();
    }
}
