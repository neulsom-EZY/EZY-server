package com.server.EZY.model.plan.errand.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.server.EZY.model.plan.embedded_type.Period;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ErrandResponseDto {
    /**
     * (간단하게) 심부름들을 반환합니다.
     *
     * @version 1.0.0
     * @author 전지환
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ErrandPreview {
        private Long planIdx;
        private String subject;
        private String title;
        private Period period;

        @QueryProjection
        public ErrandPreview(Long planIdx, String subject, String title, Period period) {
            this.planIdx = planIdx;
            this.subject = subject;
            this.title = title;
            this.period = period;
        }
    }
}
