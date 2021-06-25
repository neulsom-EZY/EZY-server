package com.server.EZY.model.plan.personal.dto;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PersonalPlanDto {
    private String planName;
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar when;
    private String where;
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
