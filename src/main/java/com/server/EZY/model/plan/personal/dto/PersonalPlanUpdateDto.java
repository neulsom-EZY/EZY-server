package com.server.EZY.model.plan.personal.dto;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
public class PersonalPlanUpdateDto {

    @NotBlank
    private String planName;
    @NotBlank
    private Calendar when;
    private String where;
    @NotBlank
    private String what;
    private String who;
    private boolean repeat;

    public PersonalPlanEntity toEntity(){
        return PersonalPlanEntity.builder()
                .planName(planName)
                .when(when)
                .where(where)
                .what(what)
                .who(who)
                .repeat(repeat)
                .build();
    }
}
