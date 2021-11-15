package com.server.EZY.model.plan.errand.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ErrandResponseDto {
    /**
     * (간단하게) 심부름들을 반환합니다.
     *
     * @version 1.0.0
     * @author 전지환
     */
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Errands {
        private Long planIdx;
        private String subject;
        private String title;
        private LocalDateTime timeToStart;
        private LocalDateTime timeToEnd;

        @QueryProjection
        public Errands(Long planIdx, String subject, String title, LocalDateTime timeToStart, LocalDateTime timeToEnd) {
            this.planIdx = planIdx;
            this.subject = subject;
            this.title = title;
            this.timeToStart = timeToStart;
            this.timeToEnd = timeToEnd;
        }
    }
}
