package com.server.EZY.exception.plan;

import com.server.EZY.exception.plan.exception.PlanNotFoundException;
import com.server.EZY.exception.token.exception.AccessTokenExpiredException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface PlanExceptionHandler {

    String PLAN_NOT_FOUND = "plan-not-found";

    // 해당 일정을 찾을 수 없습니다.
    @ExceptionHandler(PlanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult planNotFoundException(PlanNotFoundException ex);
}
