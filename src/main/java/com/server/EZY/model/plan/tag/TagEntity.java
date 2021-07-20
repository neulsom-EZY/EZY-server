package com.server.EZY.model.plan.tag;

import com.server.EZY.model.user.UserEntity;

import javax.persistence.*;

@Entity @Table(name = "tag")
public class TagEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private String tag;
}
