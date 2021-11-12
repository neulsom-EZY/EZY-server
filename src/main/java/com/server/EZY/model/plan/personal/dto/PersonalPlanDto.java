package com.server.EZY.model.plan.personal.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import com.server.EZY.model.plan.tag.embedded_type.Color;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 개인일정이라는 대분류를 가지고 세부적인 dto를 기술합니다.
 *
 * @since 1
 * @version 1
 * @author 전지환
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PersonalPlanDto {
    /**
     * 개인일정을 생성할 때 사용하는 dto 입니다.
     * @version 1
     * @since 1
     * @author 전지환
     */
    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
    public static class PersonalPlanSet {
        @NotNull
        private PlanInfo planInfo;
        @NotNull
        private Period period;
        private Long tagIdx;
        @NotNull
        private Boolean repetition;

        /**
         * 서비스 로직에서 personalPlanSave 시에 Entity set 를 위한 toEntity 메서드
         * @param memberEntity 개인 일정을 저장하고자 하는 memberEntity
         * @param tagEntity nullable
         * @return
         * @author 전지환
         */
        public PersonalPlanEntity saveToEntity(MemberEntity memberEntity, TagEntity tagEntity){
            return PersonalPlanEntity.builder()
                    .memberEntity(memberEntity)
                    .tagEntity(tagEntity)
                    .planInfo(this.planInfo)
                    .period(this.period)
                    .repetition(this.repetition)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PersonalPlanQick {

    }

    /**
     * 특정 개인일정 상세조회에 사용되는 dto 입니다.
     *
     * @since 1
     * @version 1
     * @author 전지환
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PersonalPlanDetails {
        private PlanInfo planInfo;
        private Period period;
        private Long tagIdx;
        private String tag;
        private Color color;
        private Boolean repetition;

        @QueryProjection
        public PersonalPlanDetails(PlanInfo planInfo, Period period, Long tagIdx, String tag, Color color, Boolean repetition) {
            this.planInfo = planInfo;
            this.period = period;
            this.tagIdx = tagIdx;
            this.tag = tag;
            this.color = color;
            this.repetition = repetition;
        }
    }

    /**
     * 모든 개인일정을 조회할 때 사용되는 dto 입니다.
     *
     * @since 1
     * @version 1
     * @author 전지환
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PersonalPlanListDto {
        private Long planIdx;
        private PlanInfo planInfo;
        private Period period;
        private Long tagIdx;
        private String tag;
        private Color color;
        private Boolean repetition;

        @QueryProjection
        public PersonalPlanListDto(Long planIdx, PlanInfo planInfo, Period period, Long tagIdx, String tag, Color color, Boolean repetition) {
            this.planIdx = planIdx;
            this.planInfo = planInfo;
            this.period = period;
            this.tagIdx = tagIdx;
            this.tag = tag;
            this.color = color;
            this.repetition = repetition;
        }
    }
}
