package com.server.EZY.exception.plan;

import com.server.EZY.exception.plan.exception.PlanNotFoundException;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class PlanExceptionHandlerImpl implements PlanExceptionHandler {

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @Override
    public CommonResult planNotFoundException(PlanNotFoundException ex) {
        CommonResult exceptionResponseObj = exceptionResponseObjectUtil.getExceptionResponseObj(PLAN_NOT_FOUND);
        String planType = ex.getPlanType().name();

        // Exception의 enumType에 따라 exception msg변경
        exceptionResponseObj.updateMassage(exceptionResponseObj.getMassage().replace(":planType", planType));
        return exceptionResponseObj;
    }
}
