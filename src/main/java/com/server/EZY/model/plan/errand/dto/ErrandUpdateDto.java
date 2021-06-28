package com.server.EZY.model.plan.errand.dto;

import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
import lombok.*;

import java.util.Calendar;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
public class ErrandUpdateDto {

    private String errandName;
    private String where;
    private Calendar startAt;
    private Calendar endAt;
    private ResponseStatus responseStatus;

    public ErrandEntity toEntity(){
        return ErrandEntity.builder()
                .errandName(this.errandName)
                .where(this.where)
                .startAt(this.startAt)
                .endAt(this.endAt)
                .responseStatus(this.responseStatus)
                .build();
    }
}
