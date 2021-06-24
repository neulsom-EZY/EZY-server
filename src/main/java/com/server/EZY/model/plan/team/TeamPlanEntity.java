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

    /**
     * 현재 TeamPlanEntity의 필드값을 업데이트 하는 함수다.<br>
     *
     * 현재 Entity를 업데이트하기 전에 UserEntity타입의 teamLeader를 인수로 받아 현재 변경하려는 유저가 팀리더인지 체크한다. <br>
     * 만약 팀리더가 아닌 다른 유저가 TeamPlanEntity를 변경하려 하는 경우 Exception(임시)이 발생한다. <br>
     *
     * 현재 Entity의 값을 바꾸기 위해 TeamPlanEntity타입의 updatedTeamPlanEntity를 인수로 받아 <br>
     * updatedTeamPlanEntity 안에 있는 각각의 필드의 값이 null이 아니면 현재 Entity의 값을 변경한다. <br>
     *
     * @param updateTeamPlanEntity 바꾸고 싶은 필드의 값이 들어있는 TeamPlanEntity 타입의 매개변수
     * @param currentUser Team 리더를 확인하기 위한 UserEntity 타입의 매개변수
     * @throws Exception Team 리더가 아닌 다른 유저가 현재 TeamPlanEntity를 수정하려는 경우 Exception(임시)
     * @author 정시원
     */
    public void updateTeamPlan(TeamPlanEntity updateTeamPlanEntity, UserEntity currentUser) throws Exception {
        // 팀리더만 검증
        if(this.teamLeader.getUsername().equals(currentUser.getUsername())){
            this.what = updateTeamPlanEntity.what != null ? updateTeamPlanEntity.what : this.what;
            this.planName = updateTeamPlanEntity.planName != null ? updateTeamPlanEntity.planName : this.planName;
            this.when = updateTeamPlanEntity.when != null ? updateTeamPlanEntity.when : this.when;
            this.where = updateTeamPlanEntity.where != null ? updateTeamPlanEntity.where : this.where;
        }else
            throw new Exception("팀리더만 변경할 수 있습니다.");
    }
}
