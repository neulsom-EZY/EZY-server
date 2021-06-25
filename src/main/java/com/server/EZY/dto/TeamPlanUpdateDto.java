package com.server.EZY.dto;

import com.server.EZY.model.plan.team.TeamPlanEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
public class TeamPlanUpdateDto {

    @NotBlank
    private String planName;
    private String what;
    @NotBlank
    private Calendar when;
    private String where;

    public TeamPlanEntity toEntity(){
        return TeamPlanEntity.builder()
                .planName(this.planName)
                .what(this.what)
                .when(this.when)
                .where(this.where)
                .build();
    }
}
