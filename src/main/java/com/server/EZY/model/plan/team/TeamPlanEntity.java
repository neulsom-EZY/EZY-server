package com.server.EZY.model.plan.team;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.PlanEntity;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.tag.TagEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 팀 일정을 저장하는 TeamPlanEntity
 * @author 정시원
 * @version 미정
 * @since 미정
 */
@Entity @Table(name = "team_plan")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamPlanEntity extends PlanEntity {

    /**
     * 개인일정을 추가하는 생성자
     * @param memberEntity 연관관계를 맻을 유저엔티티
     * @param tagEntity 태그를 지정하는 TagEntity타입의 객체
     * @param planInfo 개인일정의 기본적인 정보(title, explanation)을 가지고 있는 PlanInfo타입의 객체
     * @param period 개인일정의 시작/종료(startTime, endTime) 시간을 가지고 있는 Period타입의 객체
     * @author 정시원
     */
    @Builder
    public TeamPlanEntity(MemberEntity memberEntity, TagEntity tagEntity, PlanInfo planInfo, Period period){
        super(memberEntity, tagEntity, planInfo, period);
    }

    public void updateTeamPlan(TeamPlanEntity teamPlanEntity){
        planInfo.updatePlanInfo(teamPlanEntity.planInfo);
        period.updatePeriod(teamPlanEntity.period);
    }
}
