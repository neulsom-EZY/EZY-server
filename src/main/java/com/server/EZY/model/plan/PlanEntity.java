package com.server.EZY.model.plan;

import com.server.EZY.model.base_time.BaseTimeEntity;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.tag.TagEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 모든 일정 Entity의 슈퍼타입인 PlanEntity이다. <br>
 * 해당 Entity를 직접적으로 사용하지 않고 다른 Entity에 상속받아 사용한다. <br>
 * {@link BaseTimeEntity}를 상속받아 생성시간, 변경시간 필드가 추가된다.
 * @see com.server.EZY.model.plan.personal.PersonalPlanEntity
 * @see com.server.EZY.model.plan.errand.ErrandEntity
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity @Table(name = "plan")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "d_type") //d_type 자동 생성
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    protected Long planIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    protected MemberEntity memberEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = true)
    protected TagEntity tagEntity;

    @Embedded
    protected PlanInfo planInfo;

    @Embedded
    protected Period period;

    /**
     * 일정들의 기본적인 정보들을 생성하는 PlanEntity객체의 생성자
     * @param memberEntity 유저와 연관관계를 맻을 MemberEntity타입의 객체 (null 비허용)
     * @param tagEntity 태그를 지정하는 TagEntity타입의 객체 (null 허용)
     * @param planInfo 일정의 기본적인 정보(title, explanation)을 가지고 있는 PlanInfo타입의 객체 (null 비허용)
     * @param period 일정의 시작/종료(startTime, endTime) 시간을 가지고 있는 Period타입의 객체 (null 비허용)
     * @author 정시원
     */
    protected PlanEntity(MemberEntity memberEntity, TagEntity tagEntity, PlanInfo planInfo, Period period){
            this.memberEntity = memberEntity;
            this.tagEntity = tagEntity;
            this.planInfo = planInfo;
            this.period = period;
    }
}
