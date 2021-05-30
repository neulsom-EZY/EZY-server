package com.server.EZY.model.plan.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Team")
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class TeamEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TeamId")
    private Long teamIdx;

    @Column(name = "TeamLeaderId")
    private Long teamLeaderIdx;

}
