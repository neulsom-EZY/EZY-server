package com.server.EZY.model.plan.personal.dto;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.embedded_type.Period;
import com.server.EZY.model.plan.embedded_type.PlanInfo;
import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.tag.TagEntity;
import lombok.*;

import javax.validation.constraints.NotNull;

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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PersonalPlanDetails {

    }
}
