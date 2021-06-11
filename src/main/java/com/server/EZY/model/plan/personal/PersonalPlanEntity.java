package com.server.EZY.model.plan.personal;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Calendar;

@Entity @Table(name = "PersonalPlan")
@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class PersonalPlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PersonalPlanId")
    private Long personalPlanIdx;

    @Column(name = "PlanName", nullable = false)
    @Size(min = 1, max = 30)
    private String planName;

    @Column(name = "PlanWhen")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar when;

    @Column(name = "PlanWhere")
    private String where;

    @Column(name = "PlanWhat")
    private String what;

    @Column(name = "PlanWho")
    private String who;

    @Column(name = "PlanRepeat")
    private Boolean repeat;
}