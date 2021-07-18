package com.server.EZY.model.plan.personal;

import com.server.EZY.model.plan.errand.ErrandStatus;
import com.server.EZY.model.plan.headOfPlan.enumType.PlanDType;
import lombok.*;

import javax.persistence.*;
import java.util.Calendar;

import static javax.persistence.FetchType.*;

@Entity @Table(name = "NewPersonalPlan")
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class NewPersonalPlanEntity {

    @Id @Column(name = "personal_plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalPlanIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "errand_status_id")
    private ErrandStatus errandStatus;

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
    @Enumerated(EnumType.STRING)
    private PlanDType DType;
}
