package com.server.EZY.model.plan.errand;

import com.server.EZY.model.user.UserEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "Errand")
public class ErrandEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ErrandIdx")
    private Long errandIdx;

    @Column(name = "SenderId")
    private Long senderIdx;

    @Column(name = "RecipientId")
    private Long recipientIdx;

    @Column(name = "ErrandName")
    private String errandName;

    @Column(name = "ErrandsWhat")
    private String what;

    @Column(name = "ErrandWhere")
    private String where;

    @Column(name = "ErrandUntil")
    private String until;

    @Column(name = "ErrandDestination")
    private String destination;

    @Column(name = "ErrandResponseStatus")
    private ResponseStatus responseStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "UserId")
    private UserEntity userEntity;
}
