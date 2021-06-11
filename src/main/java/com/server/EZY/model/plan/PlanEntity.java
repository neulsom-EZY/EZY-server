package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.server.EZY.model.plan.PlanDType.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity @Table(name = "Plan")
@NoArgsConstructor @Getter
public class PlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlanId")
    private Long planIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UserId")
    private UserEntity userEntity;

    //PlanEntity가  저장, 병합, 삭제가 일어때 PersonalPlanEntity에 전의됩니다.
    @OneToOne(fetch = LAZY, cascade = {PERSIST, MERGE, REMOVE})
    @JoinColumn(name = "PersonalPlanId")
    private PersonalPlanEntity personalPlanEntity;

    //PlanEntity가 저장, 병합이 일어때 TeamPlanEntity에 전의됩니다.
    @ManyToOne(fetch = LAZY, cascade = {PERSIST, MERGE})
    @JoinColumn(name = "TeamPlanId")
    private TeamPlanEntity teamPlanEntity;

    // Plan을 통해 PersonalPlan을 조인할지 TeamPlan을 조인할지 결정해주는 컬럼
    @Enumerated(value = EnumType.STRING)
    @Column(name = "PlanDType")
    private PlanDType planDType;

    @Column(name = "Category")
    @ElementCollection(fetch = EAGER)
    @CollectionTable(
            name = "Category",
            joinColumns = @JoinColumn(name = "PlanId")
    )
    private List<String> categories = new ArrayList<>();

    /**
     * PersonalPlanEntity 과 UserEntity 로 객체 생성
     *
     *
     * @param userEntity
     * @param personalPlanEntity
     */
    public PlanEntity(PersonalPlanEntity personalPlanEntity, UserEntity userEntity){
        if(userEntity != null && personalPlanEntity != null && this.teamPlanEntity == null) {
            this.userEntity = userEntity;
            this.personalPlanEntity = personalPlanEntity;
            this.planDType = PERSONAL_PLAN;
        }else{
            throw new IllegalArgumentException("PersonalPlanEntity 또는 UserEntity가 null입니다.");
        }
    }
    public PlanEntity(PersonalPlanEntity personalPlanEntity, UserEntity userEntity, List<String> categories){
        this(personalPlanEntity, userEntity);
        if(categories != null)
            this.categories = categories;
        else
            throw new IllegalArgumentException("categories 가 null 입니다.");
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
            throw new IllegalArgumentException("TeamPlanEntity 또는 UserEntity가 null입니다."); // 임의로 넣은 Exception 수정예정
        }
    }
    public PlanEntity(TeamPlanEntity teamPlanEntity, UserEntity userEntity, List<String> categories){
        this(teamPlanEntity, userEntity);
        if(categories != null)
            this.categories = categories;
        else
            throw new IllegalArgumentException("categories 가 null 입니다.");
    }
}
