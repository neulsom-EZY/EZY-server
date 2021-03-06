package com.server.EZY.model.plan.errand.dto;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandDetailEntity;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class ErrandSetDto {
    @NotNull
    private PlanInfo planInfo;
    @NotNull
    private Period period;
    @NotNull
    private String recipient;

    public ErrandEntity saveToEntity(MemberEntity memberEntity, ErrandDetailEntity errandDetailEntity){
        return ErrandEntity.builder()
                .memberEntity(memberEntity)
                .tagEntity(null)
                .planInfo(planInfo)
                .period(period)
                .errandDetailEntity(errandDetailEntity)
                .build();
    }
}
