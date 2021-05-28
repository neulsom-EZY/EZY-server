package com.server.EZY.model.plan.team;

import com.server.EZY.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "UserTeam")
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class UserTeamEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserTeamId")
    private Long UserTeamIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UserId")
    private UserEntity userEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TeamId")
    private TeamEntity teamEntity;

}
