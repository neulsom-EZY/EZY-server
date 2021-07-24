package com.server.EZY.model.plan;

import com.server.EZY.model.baseTime.BaseTimeEntity;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.tag.TagEntity;

import javax.persistence.*;

@Entity @Table(name = "plan")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class PlanEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    protected Long planIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    protected MemberEntity memberEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    protected TagEntity tagEntity;

    @Embedded
    protected PlanInfo planInfo;

    @Embedded
    protected Period period;
}
