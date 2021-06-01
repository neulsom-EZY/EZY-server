package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.server.EZY.model.plan.PlanDType.*;
import static javax.persistence.FetchType.*;

@Entity
@Table(name = "Plan")
@NoArgsConstructor
@Getter
public class PlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlanId")
    private Long planIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UserId")
    private UserEntity userEntity;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "PersonalPlanId")
    private PersonalPlanEntity personalPlanEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TeamPlanId")
    private TeamPlanEntity teamPlanEntity;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "PlanDType")
    private PlanDType planDType;

    /**
     * PersonalPlanEntity 과 UserEntity 로 객체 생성
     * @param userEntity
     * @param personalPlanEntity
     */
    public PlanEntity(PersonalPlanEntity personalPlanEntity, UserEntity userEntity){
        if(userEntity != null || personalPlanEntity != null || this.teamPlanEntity == null) {
            this.userEntity = userEntity;
            this.personalPlanEntity = personalPlanEntity;
            this.planDType = PERSONAL_PLAN;
        }else{
            throw new NullPointerException();
        }
    }

    /**
     * TeamPlan 과 UserEntity 로 객체 생성
     * @param userEntity
     * @param teamPlanEntity
     */
    public PlanEntity(TeamPlanEntity teamPlanEntity, UserEntity userEntity){
        if(userEntity != null || teamPlanEntity != null || this.personalPlanEntity != null) { // null check 및 팀일정 과 단체일정이 중복되지 않도록
            this.userEntity = userEntity;
            this.teamPlanEntity = teamPlanEntity;
            this.planDType = TEAM_PLAN;
        }else{
            throw new NullPointerException(); // 임의로 넣은 Exception 수정예정
        }
    }
}
