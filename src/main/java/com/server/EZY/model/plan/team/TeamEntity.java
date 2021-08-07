package com.server.EZY.model.plan.team;

import com.server.EZY.model.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity @Table(name = "team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_plan_idx", nullable = false)
    private Long teamPlanIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "plan_id")
    private TeamPlanEntity teamPlanEntity;

    @Builder
    public TeamEntity(MemberEntity memberEntity, TeamPlanEntity teamPlanEntity){
        this.memberEntity = memberEntity;
        this.teamPlanEntity = teamPlanEntity;
    }
}
