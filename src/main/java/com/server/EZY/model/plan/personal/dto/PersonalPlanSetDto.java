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
    private Long tagIdx;
    @NotNull
    private Boolean repetition;

    /**
     * 서비스 로직에서 personalPlanSave 시에 Entity set 를 위한 toEntity 메서드
     * @param memberEntity
     * @param tagEntity
     * @return
     * @author 전지환
     */
    public PersonalPlanEntity saveToEntity(MemberEntity memberEntity, TagEntity tagEntity){
        return PersonalPlanEntity.builder()
                .memberEntity(memberEntity)
                .tagEntity(tagEntity)
                .planInfo(this.planInfo)
                .period(this.period)
                .repetition(this.repetition)
                .build();
    }
}
