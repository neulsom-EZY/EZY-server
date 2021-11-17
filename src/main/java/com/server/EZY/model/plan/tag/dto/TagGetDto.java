package com.server.EZY.model.plan.tag.dto;

import com.server.EZY.model.plan.tag.embedded_type.Color;
import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TagGetDto {
    private Long tagIdx;
    private String tag;
    private Color color;

    /**
     * tagIdx를 반환하기 위한 class
     *
     * @author 정시원
     * @version 1.0.0
     * @since 1.0.0
     */
    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
    public static class TagIdx{
        long tagIdx;
    }
}
