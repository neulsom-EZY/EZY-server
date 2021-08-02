package com.server.EZY.model.plan.errand.dto;

import com.server.EZY.model.plan.embeddedTypes.Period;
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
}
