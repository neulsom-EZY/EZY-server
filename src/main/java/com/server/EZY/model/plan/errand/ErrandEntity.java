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
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity @Table(name = "errand")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "errand")
public class ErrandEntity extends PlanEntity {

    @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = "errand_status_id", nullable = false, updatable = false)
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
}
