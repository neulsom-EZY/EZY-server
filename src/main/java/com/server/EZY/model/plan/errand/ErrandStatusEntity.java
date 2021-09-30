package com.server.EZY.model.plan.errand;

import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/**
 * 여러개의 심부름을 하나로
 * @author 정시원
 */
@Entity @Table(name = "errand_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrandStatusEntity {

    @Id @Column(name = "errand_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long errandStatusIdx;

    @Column(name = "sender_id")
    private Long senderIdx;

    @Column(name = "recipient_id")
    private Long recipientIdx;

    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    private ErrandResponseStatus errandResponseStatus;

    /**
     * 심부름의 상태를 추가하는 생성자
     * @param senderIdx 발신자의 MemberIdx
     * @param recipientIdx 수신자의 MemberIdx
     * @param errandResponseStatus 심부름의 상태
     * @author 정시원
     */
    @Builder
    public ErrandStatusEntity(Long senderIdx, Long recipientIdx, ErrandResponseStatus errandResponseStatus){
        this.senderIdx = senderIdx;
        this.recipientIdx = recipientIdx;
        this.errandResponseStatus = errandResponseStatus;
    }
}
