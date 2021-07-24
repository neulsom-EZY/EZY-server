package com.server.EZY.model.plan.personal;

import com.server.EZY.model.plan.PlanEntity;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import lombok.*;
import javax.persistence.*;

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
    public PersonalPlanEntity(MemberEntity memberEntity, TagEntity tagEntity, PlanInfo planInfo, Period period, Boolean repetition){
        super(memberEntity, tagEntity, planInfo, period);
        if(repetition != null)
            this.repetition = repetition;
        else
            throw new NullPointerException("repetition에 null값이 들어갈 수 없습니다. (ture/false 만 허용)");
    }

    /**
     * personalPlan를 업데이트 하는 매서드
     * 현재 겍체에 저장되어있는 memberEntity와 업데이트를 하려는 currentMember를 동등성 비교를 해서 이 일정의 소유자일 경우에 이 객체를 변경할 수 있다.
     * @param updatedPersonalPlanEntity 업데이트 할 PersonalPlan타입의 인자
     * @author 정시원
     */
    public void updatePersonalPlanEntity(MemberEntity currentMember, TagEntity tagEntity, PlanInfo planInfo, Period period, boolean repetition) throws Exception {
        if (this.memberEntity.equals(currentMember)) {
            this.repetition = repetition;

            if(tagEntity != null)
                this.tagEntity = tagEntity;
            if (planInfo != null)
                this.planInfo.updatePlanInfo(planInfo);
            if (period != null)
                this.period.updatePeriod(period);
        } else {
            throw new Exception("해당 일정에 대한 접근권한이 없습니다."); // Exception 추가 예정
        }
    }

}
