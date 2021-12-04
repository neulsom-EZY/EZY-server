package com.server.EZY.model.plan.team;

import com.server.EZY.model.member.MemberEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

/**
 * 회원과 팀 일정의 관계는 ManyToMany이므로 팀(TeamEntity)이라는 관계로 풀어내기 위한 TeamEntity
 * @author 정시원
 * @version 미정
 * @since 미정
 */
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
