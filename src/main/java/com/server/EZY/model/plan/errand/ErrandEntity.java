package com.server.EZY.model.plan.errand;

import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
import com.server.EZY.model.user.UserEntity;
import lombok.*;

import javax.persistence.*;

import java.util.Calendar;

import static javax.persistence.FetchType.*;

@Entity @Table(name = "Errand")
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class ErrandEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ErrandIdx")
    private Long errandIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private UserEntity senderUserEntity;

    @Column(name = "RecipientId")
    private Long recipientIdx;

    @Column(name = "ErrandName")
    private String errandName;

    @Column(name = "ErrandWhere")
    private String where;

    @Column(name = "ErrandUntil")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startAt;

    @Column(name = "ErrandDestination")
    private Calendar endAt;

    @Column(name = "ErrandResponseStatus")
    private ResponseStatus responseStatus;

    public void updateErrand(ErrandEntity updatedErrandEntity){
        this.errandName = updatedErrandEntity.errandName != null ? updatedErrandEntity.errandName : this.errandName;
        this.where = updatedErrandEntity.where != null ? updatedErrandEntity.where : this.where;
        this.startAt = updatedErrandEntity.startAt != null ? updatedErrandEntity.startAt : this.startAt;
        this.endAt = updatedErrandEntity.endAt != null ? updatedErrandEntity.endAt : this.endAt;
        this.responseStatus = updatedErrandEntity.responseStatus != null ? updatedErrandEntity.responseStatus : this.responseStatus;
    }
}
