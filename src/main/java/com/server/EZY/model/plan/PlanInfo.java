package com.server.EZY.model.plan;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class PlanInfo {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "explanation", nullable = true)
    private String explanation;

    @Embedded
    private Period period;

}
