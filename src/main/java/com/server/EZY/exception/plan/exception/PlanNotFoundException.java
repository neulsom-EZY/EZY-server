package com.server.EZY.exception.plan.exception;

import com.server.EZY.model.plan.enum_type.PlanType;
import lombok.Getter;

/**
 * 심부름, 개인일정 등.. 일정이 존재하지 않을 떄 사용한다. <br>
 * 생성자로 PlanType를 넘겨 어떤 일정이 존재하지 않는지 알린다.
 *
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
public class PlanNotFoundException extends RuntimeException {

    private PlanType planType;

    public PlanNotFoundException(PlanType planType){
        this.planType = planType;
    }
}
