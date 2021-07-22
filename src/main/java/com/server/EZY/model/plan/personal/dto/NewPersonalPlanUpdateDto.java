package com.server.EZY.model.plan.personal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.EZY.model.plan.Period;
import com.server.EZY.model.plan.PlanInfo;
import com.server.EZY.model.plan.personal.NewPersonalPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.*;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
public class NewPersonalPlanUpdateDto {

    @JsonIgnore
    private UserEntity userEntity;

    private PlanInfo planInfo;
    private Period period;
    private boolean repetition;

    public NewPersonalPlanEntity toEntity(){
        return new NewPersonalPlanEntity(userEntity, planInfo, period, repetition);
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
