package com.server.EZY.model.plan.tag.dto;

import com.server.EZY.model.plan.tag.embedded_type.Color;
import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TagGetDto {
    private Long tagIdx;
    private String tag;
    private Color color;
}
