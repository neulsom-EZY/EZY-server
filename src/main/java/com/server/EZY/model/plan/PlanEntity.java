package com.server.EZY.model.plan;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.team.TeamPlanEntity;
import com.server.EZY.model.user.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.server.EZY.model.plan.PlanDType.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Slf4j
@Entity @Table(name = "Plan")
@NoArgsConstructor @Getter
public class PlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PlanId")
    private Long planIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UserId")
    private UserEntity userEntity;

    //PlanEntity가  저장, 병합, 삭제가 발생될때 PersonalPlanEntity에 전의됩니다.
    @OneToOne(fetch = LAZY, cascade = {PERSIST, MERGE, REMOVE})
    @JoinColumn(name = "PersonalPlanId")
    private PersonalPlanEntity personalPlanEntity;

    //PlanEntity가 저장, 병합이 발생될때 TeamPlanEntity에 전의됩니다.
    @ManyToOne(fetch = LAZY, cascade = {PERSIST, MERGE})
    @JoinColumn(name = "TeamPlanId")
    private TeamPlanEntity teamPlanEntity;

    // 어떠한 join할 테이블의 타입 알려줌
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
            log.debug("=== PersonalPlanEntity 또는 UserEntity가 null입니다. ===");
            log.debug("userEntity = {} ", userEntity);
            log.debug("personalPlanEntity = {} ", personalPlanEntity);
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
     * 팀일정(TeamPlan)을 생성하기 위한 생성자 (Category 제외)
     * @param teamPlanEntity 팀일정을 만들기 위한 TeamPlanEntity 타입의 매개변수
     * @param userEntity 어떤 유저의 일정인지 연관관계를 맻는 UserEntity 타입의 매개변수
     * @throws IllegalArgumentException TeamPlanEntity 혹은 UserEntity가 null일경우 발생(임시)
     * @author 정시원
     */
    public PlanEntity(TeamPlanEntity teamPlanEntity, UserEntity userEntity){
        if(userEntity != null && teamPlanEntity != null) { // null check 및 팀일정 과 단체일정이 중복되지 않도록
            this.userEntity = userEntity;
            this.teamPlanEntity = teamPlanEntity;
            this.planDType = TEAM_PLAN;
        }else{
            log.debug("=== TeamPlanEntity 또는 UserEntity가 null입니다. ===");
            log.debug("userEntity = {} ", userEntity);
            log.debug("teamPlanEntity = {} ", teamPlanEntity);
            throw new IllegalArgumentException("TeamPlanEntity 또는 UserEntity가 null입니다."); // 임의로 넣은 Exception 수정예정
        }
    }
    /**
     * 팀일정(TeamPlan)을 생성하기 위한 생성자 TeamPlanEntity UserEntity를 매개변수로 받는 생성자를 호출한다. (Category 포함)
     * @param teamPlanEntity 팀일정(TeamPlan)을 만들기 위한 TeamPlanEntity 타입의 매개변수
     * @param userEntity 어떤 유저의 일정인지 연관 관계를 맻는 UserEntity 타입의 매개변수
     * @param categories 현재 일정에 대한 카테고리를 지정하는 List&#60;String&#62;타입의 매개변수
     * @throws IllegalArgumentException TeamPlanEntity 혹은 UserEntity가 null일경우 발생(임시)
     * @throws IllegalArgumentException List&#60;String&#62;타입의 categories가 null일경우
     * @author 정시원
     */
    public PlanEntity(TeamPlanEntity teamPlanEntity, UserEntity userEntity, List<String> categories){
        this(teamPlanEntity, userEntity);
        if(categories != null)
            this.categories = categories;
        else
            throw new IllegalArgumentException("categories 가 null 입니다.");
    }
}
