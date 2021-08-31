package com.server.EZY.model.plan.tag.dto;

import com.server.EZY.model.plan.tag.embedded_type.Color;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class TagGetDto {
    @NotNull
    private Color color;
    @NotNull
    private String tag;
}
