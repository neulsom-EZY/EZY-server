package com.server.EZY.model.baseTime;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass @EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate @Column(name = "create_date", updatable = false)
    private LocalDateTime createdDate;

    @CreatedDate @Column(name = "modified_date", updatable = false)
    private LocalDateTime modifiedDate;
}