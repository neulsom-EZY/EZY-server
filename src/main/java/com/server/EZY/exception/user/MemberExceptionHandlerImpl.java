package com.server.EZY.exception.user;

import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.user.exception.MemberAlreadyExistException;
import com.server.EZY.exception.user.exception.MemberNotFoundException;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.util.ExceptionResponseObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice @Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class MemberExceptionHandlerImpl implements MemberExceptionHandler {

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

    @Override
    public CommonResult memberNotFoundException(MemberNotFoundException ex){
        log.debug("=== User Not Found Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(MEMBER_NOT_FOUND);
    }

    @Override
    public CommonResult memberAlreadyExistException(MemberAlreadyExistException ex) {
        log.debug("=== Member Already Exist Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(MEMBER_ALREADY_EXIST);
    }

    @Override
    public CommonResult usernameNotFoundException(UsernameNotFoundException ex) {
        log.debug("=== Username Not Found Exception 발생 ===");
        CommonResult usernameNotFoundExceptionResponseObj = exceptionResponseObjectUtil.getExceptionResponseObj(USERNAME_NOT_FOUND);
        // UsernameNotFoundExceptiond에서 해당 username으로 회원을 찾지 못했을 경우 해당 username를 Exception message에 포함하는 로직
        String insertUsernameInExceptionMassage =
                usernameNotFoundExceptionResponseObj.getMassage().replaceAll(":username", "'" + ex.getMessage() + "'");
        usernameNotFoundExceptionResponseObj.updateMassage(insertUsernameInExceptionMassage);

        return usernameNotFoundExceptionResponseObj;
    }

    @Override
    public CommonResult invalidAccessException(InvalidAccessException ex) {
        log.debug("=== InvalidAccessException 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(INVALID_ACCESS);
    }
}
