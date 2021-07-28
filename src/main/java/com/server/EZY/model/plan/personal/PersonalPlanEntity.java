package com.server.EZY.model.plan.personal;

import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.model.plan.PlanEntity;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity @Table(name = "personal_plan")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalPlanEntity extends PlanEntity {

    @Column(name = "repetition")
    private Boolean repetition;

    /**
     * 개인일정을 추가하는 생성자
     * @param memberEntity 연관관계를 맻을 유저엔티티
     * @param tagEntity 태그를 지정하는 TagEntity타입의 객체
     * @param planInfo 개인일정의 기본적인 정보(title, explanation)을 가지고 있는 PlanInfo타입의 객체
     * @param period 개인일정의 시작/종료(startTime, endTime) 시간을 가지고 있는 Period타입의 객체
     * @param repetition 반복여부
     * @author 정시원
     */
    @Builder
    public PersonalPlanEntity(MemberEntity memberEntity, TagEntity tagEntity, PlanInfo planInfo, Period period, boolean repetition){
        super(memberEntity, tagEntity, planInfo, period);
        this.repetition = repetition;
    }

    /**
     * personalPlan를 업데이트 하는 매서드
     * 현재 변경하려는 personalPlan에 연관관계를 맻고있는 회원과 이 일정을 변경하려는 회원를 동등성 비교를 통해 현재 일정의 소유자여부를 판별한 후 객체를 업데이트 한다.
     * @param personalPlanEntity 변경할 필드를 가지고 있는 PersonalPlanEntity 주로 DTO에서 toEntity 매서드로 생성된 PersonalPlanEntity를 인수로 넘겨받는다.
     * @author 정시원
     */
    public void updatePersonalPlanEntity(PersonalPlanEntity personalPlanEntity) {
        this.tagEntity = personalPlanEntity.tagEntity != null ? personalPlanEntity.tagEntity : this.tagEntity;
        this.planInfo = personalPlanEntity.planInfo != null ? personalPlanEntity.planInfo : this.planInfo;
        this.period = personalPlanEntity.period != null ? personalPlanEntity.period : this.period;
        this.repetition =  personalPlanEntity.repetition != null ? personalPlanEntity.repetition : this.repetition;
    }

}
