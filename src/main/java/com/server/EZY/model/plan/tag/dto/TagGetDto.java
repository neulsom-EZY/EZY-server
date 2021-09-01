package com.server.EZY.model.plan.tag.dto;

import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.embedded_type.Color;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class TagGetDto {
    @NotNull
    private Color color;
    @NotNull
    private String tag;

    public TagEntity getToEntity(){
        return TagEntity.builder()
                .tag(this.tag)
                .color(this.color)
                .build();
    }
}
