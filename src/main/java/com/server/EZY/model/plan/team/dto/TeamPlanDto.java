package com.server.EZY.model.plan.team.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeamPlanDto {
    @JsonIgnore
    private UserEntity teamLeader;
    @NotBlank
    private String planName;
    private String what;
    @NotBlank
    private Calendar when;
    private String where;

    public TeamPlanEntity toEntity(){
        return TeamPlanEntity.builder()
                .teamLeader(this.teamLeader)
                .planName(this.planName)
                .what(this.what)
                .when(this.when)
                .where(this.where)
                .build();
    }
}
