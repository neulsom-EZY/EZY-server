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
 * ErrandDetailEntity로 연관관계를 맺어 두개의 컬럼을 하나의 심부름으로 식별할 수 있다.
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
    private ErrandDetailEntity errandDetailEntity;

    /**
     * 심부름을 추가하는 생성자
     * @param memberEntity 연관관계를 맻을 유저엔티티
     * @param tagEntity 심부름의 태그를 지정하는 TagEntity타입의 객체
     * @param planInfo 심부름의 기본적인 정보(title, explanation)을 가지고 있는 PlanInfo타입의 객체
     * @param period 심부름의 시작/종료(startTime, endTime) 시간을 가지고 있는 Period타입의 객체
     * @param errandDetailEntity 심부름의 상태 및 유저정보들을 가지고 있는 Entity
     * @author 정시원
     */
    @Builder
    public ErrandEntity(MemberEntity memberEntity, TagEntity tagEntity, PlanInfo planInfo, Period period, ErrandDetailEntity errandDetailEntity){
        super(memberEntity, tagEntity, planInfo, period);
        this.errandDetailEntity = errandDetailEntity;
    }

    /**
     * MemberEntity와 planIdx를 제외한 후 깊은 복사를 진행한다. <br>
     * <br>
     *
     * 복제가 제외된 필드
     * <ul>
     *     <li>memberEntity: 메서드에서 인수로 받은 memberEntity로 설정한다.</li>
     *     <li>planIdx는: Entity를 식별할 수 있는 필드이므로 중복을 허용하지 않아 null로 설정한다. </li>
     * <ul/>
     * @param memberEntity 복제할 ErrandEntity에 해당 memberEntity를 설정합니다.
     * @return planIdx가 null이고 memberEntity가 변경되어 clone된 ErrandEntity
     * @author 정시원
     */
    public ErrandEntity cloneToMemberEntity(MemberEntity memberEntity){
        ErrandEntity clonedErrandEntity = ErrandEntity.builder()
                .memberEntity(memberEntity)
                .tagEntity(null)
                .planInfo(this.planInfo)
                .period(this.period)
                .errandDetailEntity(this.errandDetailEntity)
                .build();
        clonedErrandEntity.planIdx = null;
        return clonedErrandEntity;
    }
}
