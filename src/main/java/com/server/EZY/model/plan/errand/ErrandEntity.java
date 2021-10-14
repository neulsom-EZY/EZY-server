package com.server.EZY.model.plan.errand;

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
 * 심부름 일정을 저장하는 Entity이다.<br>
 * 심부름 일정 추가시 수신자/발신자 총 2개의 컬럼이 추가되므로 이를 하나로 식별할 수 있게
 * ErrandStatusEntity로 연관관계를 맺어 두개의 컬럼을 하나의 심부름으로 식별할 수 있다.
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity @Table(name = "errand")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "errand")
public class ErrandEntity extends PlanEntity {

    @ManyToOne @JoinColumn(name = "errand_status_id", nullable = false, updatable = false)
    private ErrandStatusEntity errandStatusEntity;

    private String location;

    /**
     * 심부름을 추가하는 생성자
     * @param memberEntity 연관관계를 맻을 유저엔티티
     * @param tagEntity 심부름의 태그를 지정하는 TagEntity타입의 객체
     * @param planInfo 심부름의 기본적인 정보(title, explanation)을 가지고 있는 PlanInfo타입의 객체
     * @param period 심부름의 시작/종료(startTime, endTime) 시간을 가지고 있는 Period타입의 객체
     * @param errandStatusEntity 심부름의 상태 및 유저정보들을 가지고 있는 Entity
     * @param location 심부름의 위치를 나타내는 location
     * @author 정시원
     */
    @Builder
    public ErrandEntity(MemberEntity memberEntity, TagEntity tagEntity, PlanInfo planInfo, Period period, ErrandStatusEntity errandStatusEntity, String location){
        super(memberEntity, tagEntity, planInfo, period);
        this.errandStatusEntity = errandStatusEntity;
        this.location = location;
    }

    /**
     * MemberEntity를 기준으로 현제 객체를 복사해 다른 객체로 반환합니다.
     * @param memberEntity 복사한 Entity에 set할 member
     * @return planIdx가 null이고 memberEntity가 변경되어 clone된 entity
     * @author 정시원
     */
    public ErrandEntity cloneByMember(MemberEntity memberEntity){
        ErrandEntity clonedErrandEntity = ErrandEntity.builder()
                .memberEntity(memberEntity)
                .tagEntity(null)
                .planInfo(planInfo)
                .period(period)
                .errandStatusEntity(errandStatusEntity)
                .location(location)
                .build();
        clonedErrandEntity.setPlanIdx(null);
        return clonedErrandEntity;
    }

    /**
     * planIdx에 대한 setter
     * @param planIdx
     * @author 정시원
     */
    private void setPlanIdx(Long planIdx){
        this.planIdx = planIdx;
    }
}
