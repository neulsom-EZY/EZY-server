package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.server.EZY.model.plan.PlanDType.*;
import static javax.persistence.CascadeType.*;
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

    //PersonalPlan은 Plan과 1 : 1관계 이므로 Plan이 삭제되면 PersonalPlan이 삭제되야되고 역으로 PersonalPlan이 삭제되면 Plan이 삭제돼야 된다.
    @OneToOne(fetch = LAZY, cascade = {PERSIST, MERGE, REMOVE})
    @JoinColumn(name = "PersonalPlanId")
    private PersonalPlanEntity personalPlanEntity;

    // User : Plan : TeamPlan = N : 1 : M
    //TeamPlan는 Plan에서 여러명의 User가 TeamPlan을 가르키므로 한사람의 PlanEntity가 삭제됨으로 인하여 TeamPlan 자체가 삭제되면 안되므로 REMOVE를 제외했다.
    @ManyToOne(fetch = LAZY, cascade = {MERGE, PERSIST})
    @JoinColumn(name = "TeamPlanId")
    private TeamPlanEntity teamPlanEntity;

    // Plan을 통해 PersonalPlan을 조인할지 TeamPlan을 조인할지 결정해주는 컬럼
    @Enumerated(value = EnumType.STRING)
    @Column(name = "PlanDType")
    private PlanDType planDType;

    /**
     * PersonalPlanEntity 과 UserEntity 로 객체 생성
     * @param userEntity
     * @param personalPlanEntity
     */
    public PlanEntity(PersonalPlanEntity personalPlanEntity, UserEntity userEntity){
        if(userEntity != null && personalPlanEntity != null && this.teamPlanEntity == null) {
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
        if(userEntity != null && teamPlanEntity != null && this.personalPlanEntity != null) { // null check 및 팀일정 과 단체일정이 중복되지 않도록
            this.userEntity = userEntity;
            this.teamPlanEntity = teamPlanEntity;
            this.planDType = TEAM_PLAN;
        }else{
            throw new NullPointerException(); // 임의로 넣은 Exception 수정예정
        }
    }
}
