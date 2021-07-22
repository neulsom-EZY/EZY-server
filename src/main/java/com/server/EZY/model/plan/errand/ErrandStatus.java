package com.server.EZY.model.plan.errand;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Table(name = "errand_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrandStatus {

    @Id @Column(name = "errand_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errandIdx;

    @Column(name = "sender_id")
    private Long senderIdx;

    @Column(name = "recipient_id")
    private Long recipientIdx;

}
