package com.server.EZY.exception.plan;

import com.server.EZY.exception.plan.exception.PlanNotFoundException;
import com.server.EZY.response.result.CommonResult;

public interface PlanExceptionHandler {

    String PLAN_NOT_FOUND = "plan-not-found";

    CommonResult planNotFoundException(PlanNotFoundException ex);
}
