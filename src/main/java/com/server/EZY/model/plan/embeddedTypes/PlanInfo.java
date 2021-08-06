package com.server.EZY.model.plan.embeddedTypes;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * 일정(Plan)의 정보를 임베디드 타입으로 분리한 클래스
 * @author 정시원
 */
@Embeddable
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class PlanInfo {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "explanation", nullable = true)
    private String explanation;

    @Column(name = "location", nullable = true)
    private String location;

    /**
     * PlanInfo를 업데이트 하는 매서드
     * @param updatedPlanInfo 업데이트 할 PlanInfo타입의 인자
     * @author 정시원
     */
    public void updatePlanInfo(PlanInfo updatedPlanInfo){
        this.title = updatedPlanInfo.title != null ? updatedPlanInfo.title : this.title;
        this.explanation = updatedPlanInfo.explanation != null ? updatedPlanInfo.explanation : this.explanation;
        this.location = updatedPlanInfo.location != null ? updatedPlanInfo.location : this.location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanInfo)) return false;
        PlanInfo planInfo = (PlanInfo) o;
        return Objects.equals(getTitle(), planInfo.getTitle()) && Objects.equals(getExplanation(), planInfo.getExplanation()) && Objects.equals(getLocation(), planInfo.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getExplanation(), getLocation());
    }
}
