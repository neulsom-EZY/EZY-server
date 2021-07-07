package com.server.EZY.exception.custom;

import com.server.EZY.exception.custom.exception.CustomForbiddenException;
import com.server.EZY.exception.custom.exception.CustomNotFoundException;
import com.server.EZY.response.result.CommonResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

@RestController
public class BasicErrorController implements ErrorController {

    @GetMapping("/error")
    public void handleError(HttpServletRequest req)  {
        Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null){
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == NOT_FOUND.value()) throw new CustomNotFoundException();
            else if(statusCode == FORBIDDEN.value()) throw new CustomForbiddenException();
        }
    }


}
