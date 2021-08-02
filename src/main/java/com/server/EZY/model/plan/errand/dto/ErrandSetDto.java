package com.server.EZY.model.plan.errand.dto;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.errand.ErrandEntity;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class ErrandSetDto {
    @NotNull
    private String title;
    @NotNull
    private Period period;
    @NotNull
    private String recipient;

//    public ErrandEntity saveToEntity(MemberEntity memberEntity){
//        return ErrandEntity.builder()
//                .memberEntity(memberEntity)
//                .tagEntity()
//                .planInfo()
//                .period()
//                .errandStatusEntity()
//                .build();
//    }
}
