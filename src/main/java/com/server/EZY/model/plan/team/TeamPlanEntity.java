package com.server.EZY.model.plan.team;

import com.server.EZY.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Calendar;

@Entity @Table(name = "TeamPlan")
@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class TeamPlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TeamId")
    private Long teamIdx;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private UserEntity teamLeader;

    @Column(name = "PlanName", nullable = false)
    @Size(min = 1, max = 30)
    private String planName;

    @Column(name = "PlanWhat")
    private String what;

    @Column(name = "PlanWhen", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar when;

    @Column(name = "PlanWhere")
    private String where;

    public void updateTeamPlan(TeamPlanEntity updatedTeamPlanEntity, UserEntity teamLeader) throws Exception {
        // 팀리더만 검증
        if(this.teamLeader.getUsername().equals(teamLeader.getUsername())){
            this.what = updatedTeamPlanEntity.what != null ? updatedTeamPlanEntity.what : this.what;
            this.planName = updatedTeamPlanEntity.planName != null ? updatedTeamPlanEntity.planName : this.planName;
            this.when = updatedTeamPlanEntity.when != null ? updatedTeamPlanEntity.when : this.when;
            this.where = updatedTeamPlanEntity.where != null ? updatedTeamPlanEntity.where : this.where;
        }else
            throw new Exception("팀리더만 변경할 수 있습니다.");
    }
}
