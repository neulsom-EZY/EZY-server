package com.server.EZY.model.plan.tag;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.tag.embedded_type.Color;
import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public TagEntity(MemberEntity memberEntity, Color color, String tag){
        this.memberEntity = memberEntity;
        this.color = color;
        this.tag = tag;
    }

}
