package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.server.EZY.model.plan.PlanDType.*;
import static javax.persistence.FetchType.*;

@Entity
@Table(name = "Plan")
@Builder
@NoArgsConstructor @AllArgsConstructor
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
     * TeamPlanEntity 와 연관관계 편의 메서드
     *
     * @param teamPlanEntity
     */
    public void updateTeamPlanEntity(TeamPlanEntity teamPlanEntity){
        if(teamPlanEntity != null || personalPlanEntity == null || planDType != PERSONAL_PLAN){
            this.teamPlanEntity = teamPlanEntity;
            this.planDType = PlanDType.TEAM_PLAN;
        }else
            throw new NullPointerException();

    }
}
