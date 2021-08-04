package com.server.EZY.model.plan.embeddedTypes;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 일정(Plan)의 기간을 임베디드 타입으로 분리한 클래스
 * @author 정시원
 */
@Embeddable
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class Period {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    /**
     * Period객체를 업데이트 한다.
     * @param updatedPeriod 업데이트 할 Period타입의 인자
     * @author 정시원
     */
    public void updatePeriod(Period updatedPeriod){
        this.startDateTime = updatedPeriod.startDateTime != null ? updatedPeriod.startDateTime : this.startDateTime;
        this.endDateTime = updatedPeriod.endDateTime != null ? updatedPeriod.endDateTime : this.endDateTime;
    }

    @Override @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Period)) return false;
        Period period = (Period) o;
        return Objects.equals(getStartDateTime(), period.getStartDateTime()) && Objects.equals(getEndDateTime(), period.getEndDateTime());
    }

    @Override @Generated
    public int hashCode() {
        return Objects.hash(getStartDateTime(), getEndDateTime());
    }
}
