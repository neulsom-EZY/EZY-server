package com.server.EZY.model.plan.errand;

import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import lombok.*;

import javax.persistence.*;

/**
 * 심부름의 일정정보를 저장하는 {@link ErrandEntity}의 상태를 관리하는 {@link ErrandStatusEntity}이다. <br>
 * {@link ErrandEntity}는 수신자, 발신자 총 2개의 컬럼을 추가하므로 하나의 심부름을 식별하고 추가적인 정보를 저장하기 위해 해당 Entity가 필요하다.
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity @Table(name = "errand_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true) // @ToString.Include 를 명시한 필드만 toString에 포함합니다.
public class ErrandStatusEntity {

    @Id @Column(name = "errand_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long errandStatusIdx;

    @Column(name = "sender_id")
    @ToString.Include
    private Long senderIdx;

    @Column(name = "recipient_id")
    @ToString.Include
    private Long recipientIdx;

    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private ErrandResponseStatus errandResponseStatus;

    public void updateErrandResponseStatus(ErrandResponseStatus errandResponseStatus){
        this.errandResponseStatus = errandResponseStatus;
    }
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
