package com.server.EZY.model.plan.tag;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.embeddedTypes.Color;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "tag")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class TagEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Embedded
    private Color color;

    private String tag;
}
