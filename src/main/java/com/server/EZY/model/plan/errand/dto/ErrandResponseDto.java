package com.server.EZY.model.plan.errand.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.errand.enum_type.ErrandStatus;
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

    /**
     * (자세하게) 심부름을 반환합니다.
     *
     * @version 1.0.0
     * @author 전지환
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ErrandDetails {
        private Long errandIdx;
        private PlanInfo planInfo;
        private Period period;
        private String sender;
        private String recipient;
        private ErrandStatus errandStatus;

        @QueryProjection
        public ErrandDetails(Long errandIdx, PlanInfo planInfo, Period period, String sender, String recipient, ErrandStatus errandStatus) {
            this.errandIdx = errandIdx;
            this.planInfo = planInfo;
            this.period = period;
            this.sender = sender;
            this.recipient = recipient;
            this.errandStatus = errandStatus;
        }
    }
}
