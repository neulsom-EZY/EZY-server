package com.server.EZY.model.baseTime;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass @EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate @Column(name = "create_date_time", updatable = false, nullable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate @Column(name = "modified_date_time",  nullable = false)
    private LocalDateTime modifiedDateTime;
}
