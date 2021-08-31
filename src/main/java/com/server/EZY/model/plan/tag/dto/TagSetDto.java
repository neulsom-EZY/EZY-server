package com.server.EZY.model.plan.tag.dto;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.embedded_type.Color;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Builder @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class TagSetDto {
    @NotNull
    private Color color;
    @NotNull
    private String tag;

    public TagEntity saveToEntity(MemberEntity memberEntity){
        return TagEntity.builder()
                .memberEntity(memberEntity)
                .tag(this.tag)
                .color(this.color)
                .build();
    }
}
