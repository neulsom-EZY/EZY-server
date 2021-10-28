package com.server.EZY.model.plan.errand;

import com.server.EZY.model.plan.errand.enum_type.ErrandStatus;
import lombok.*;

import javax.persistence.*;

/**
 * 심부름의 일정정보를 저장하는 {@link ErrandEntity}의 상태를 관리하는 {@link ErrandDetailEntity}이다. <br>
 * {@link ErrandEntity}는 수신자, 발신자 총 2개의 컬럼을 추가하므로 하나의 심부름을 식별하고 추가적인 정보를 저장하기 위해 해당 Entity가 필요하다.
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity @Table(name = "errand_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true) // @ToString.Include 를 명시한 필드만 toString에 포함합니다.
public class ErrandDetailEntity {

    @Id @Column(name = "errand_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long errandDetailIdx;

    @Column(name = "sender_id")
    @ToString.Include
    private Long senderIdx;

    @Column(name = "recipient_id")
    @ToString.Include
    private Long recipientIdx;

    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private ErrandStatus errandStatus;

    public void updateErrandStatus(ErrandStatus errandStatus){
        this.errandStatus = errandStatus;
    }
    /**
     * 심부름의 상태를 추가하는 생성자
     * @param senderIdx 발신자의 MemberIdx
     * @param recipientIdx 수신자의 MemberIdx
     * @param errandStatus 심부름의 상태
     * @author 정시원
     */
    @Builder
    public ErrandDetailEntity(Long senderIdx, Long recipientIdx, ErrandStatus errandStatus){
        this.senderIdx = senderIdx;
        this.recipientIdx = recipientIdx;
        this.errandStatus = errandStatus;
    }
}
