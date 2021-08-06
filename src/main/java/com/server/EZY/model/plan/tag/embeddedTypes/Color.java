package com.server.EZY.model.plan.tag.embeddedTypes;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Color {

    @Column(name = "color_red", nullable = false, updatable = false)
    private Short red;

    @Column(name = "color_green", nullable = false, updatable = false)
    private Short green;

    @Column(name = "color_blue", nullable = false, updatable = false)
    private Short blue;

}
