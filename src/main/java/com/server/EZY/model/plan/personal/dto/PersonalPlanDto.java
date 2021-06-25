package com.server.EZY.model.plan.personal.dto;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PersonalPlanDto {
    @NotBlank
    private String planName;
    @NotBlank
    private Calendar when;
    private String where;
    @NotBlank
    private String what;
    private String who;
    private Boolean repeat;

    public PersonalPlanEntity toEntity(){
        return PersonalPlanEntity.builder()
                .planName(this.planName)
                .when(this.when)
                .where(this.where)
                .what(this.what)
                .who(this.who)
                .repeat(this.repeat)
                .build();
    }
}
