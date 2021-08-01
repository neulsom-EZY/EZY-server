package com.server.EZY.exception.customError.controller;

import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

/**
 * SpringBoot에서 white label error를 custom한 결과로 반환하는 Controller
 * @author 정시원
 */
@RestController
public class BasicErrorController implements ErrorController {

    @GetMapping("/error")
    public void handleError(HttpServletRequest req)  {
        Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null){
            int statusCode = Integer.parsㅎeInt(status.toString());

            if(statusCode == NOT_FOUND.value()) throw new CustomNotFoundException();
            else if(statusCode == FORBIDDEN.value()) throw new CustomForbiddenException();
            else if(statusCode == UNAUTHORIZED.value()) throw new CustomUnauthorizedException();
        }
    }
}
