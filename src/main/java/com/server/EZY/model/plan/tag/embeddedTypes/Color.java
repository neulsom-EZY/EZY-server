package com.server.EZY.model.plan.tag.embeddedTypes;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import javax.validation.constraints.Size;

@Embeddable
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Color {

    @Column(name = "color_red", nullable = false, updatable = false)
    @Size(min=0, max=255)
    private Short red;

    @Column(name = "color_green", nullable = false, updatable = false)
    @Size(min=0, max=255)
    private Short green;

    @Column(name = "color_blue", nullable = false, updatable = false)
    @Size(min=0, max=255)
    private Short blue;

}
