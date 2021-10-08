package com.server.EZY.model.base_time;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 해당 Entity를 상속한 Entity는 생성시간, 수정시간 필드를 추가한다. <br>
 * 생성시간(createdDateTime)는 해당 컬럼이 insert된 시점의 시간을 나타낸다. <br>
 * 수정시간(modifiedDateTime)는 해당 컬럼이 update된 시점의 시간을 나타낸다.
 * @see com.server.EZY.model.plan.PlanEntity
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@MappedSuperclass @EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseTimeEntity {

    @CreatedDate @Column(name = "create_date_time", updatable = false, nullable = false)
    private LocalDateTime createdDateTime;

    @LastModifiedDate @Column(name = "modified_date_time",  nullable = false)
    private LocalDateTime modifiedDateTime;
}
