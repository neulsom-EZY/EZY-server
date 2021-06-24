package com.server.EZY.model.plan.personal;

import lombok.*;

import javax.persistence.*;
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

    /**
     * 현재 PersonalPlan의 필드값을 업데이트 하는 함수다.<br>
     * 현재 Entity의 값을 바꾸기 위해 PersonalPlanEntity타입의 updatePersonalPlan를 인수로 받아 <br>
     * updatedTeamPlanEntity 안에 있는 각각의 필드의 값이 null이 아니면 현재 Entity의 값을 변경한다.<br>
     * @param updatePersonalPlan 바꾸고 싶은 필드의 값이 들어있는 TeamPlanEntity 타입의 매개변수
     * @auther 정시원
     */
    public void updatePersonalPlan(PersonalPlanEntity updatePersonalPlan){
        this.planName = updatePersonalPlan.planName != null ? updatePersonalPlan.planName : this.planName;
        this.when = updatePersonalPlan.when != null ? updatePersonalPlan.when : this.when;
        this.where = updatePersonalPlan.where != null ? updatePersonalPlan.where : this.where;
        this.what = updatePersonalPlan.what != null ? updatePersonalPlan.what : this.what;
        this.who = updatePersonalPlan.who != null ? updatePersonalPlan.who : this.who;
        this.repeat = updatePersonalPlan.repeat != null ? updatePersonalPlan.repeat : this.repeat;
    }
}