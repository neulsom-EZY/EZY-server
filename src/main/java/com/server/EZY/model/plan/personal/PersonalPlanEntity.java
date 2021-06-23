package com.server.EZY.model.plan.personal;

import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
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
//    @Size(min = 1, max = 30)
    private String planName;

    @Column(name = "PlanWhen", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar when;

    @Column(name = "PlanWhere")
    private String where;

    @Column(name = "PlanWhat", nullable = false)
    private String what;

    @Column(name = "PlanWho")
    private String who;

    @Column(name = "PlanRepeat")
    private Boolean repeat;

    public void updatePersonalPlan(PersonalPlanEntity updatePersonalPlan){
        this.planName = updatePersonalPlan.planName != null ? updatePersonalPlan.planName : this.planName;
        this.when = updatePersonalPlan.when != null ? updatePersonalPlan.when : this.when;
        this.where = updatePersonalPlan.where != null ? updatePersonalPlan.where : this.where;
        this.what = updatePersonalPlan.what != null ? updatePersonalPlan.what : this.what;
        this.who = updatePersonalPlan.who != null ? updatePersonalPlan.who : this.who;
        this.repeat = updatePersonalPlan.repeat != null ? updatePersonalPlan.repeat : this.repeat;
    }
}