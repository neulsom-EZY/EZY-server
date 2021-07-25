package com.server.EZY.model.plan.errand;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embeddedTypes.Period;
import com.server.EZY.model.plan.embeddedTypes.PlanInfo;
import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
import com.server.EZY.model.plan.tag.TagEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Table(name = "errand_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrandStatusEntity {

    @Id @Column(name = "errand_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errandIdx;

    @Column(name = "sender_id")
    private Long senderIdx;

    @Column(name = "recipient_id")
    private Long recipientIdx;

    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus;

}
