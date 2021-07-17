package com.server.EZY.model.plan.personal;

import com.server.EZY.model.plan.headOfPlan.enumType.PlanDType;
import lombok.*;

import javax.persistence.*;
import java.util.Calendar;

@Entity @Table(name = "NewPersonalPlan")
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class NewPersonalPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_plan_id")
    private Long personalPlanIdx;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endTime;

    @Column(name = "explanation", nullable = true)
    private String explanation;

    @Column(name = "repetition")
    private Boolean repetition;

    @Column(name = "d_type")
    @Enumerated(EnumType.ORDINAL)
    private PlanDType DType;
}
