package com.server.EZY.exception;

import com.server.EZY.exception.authenticationNumber.exception.AuthenticationNumberTransferFailedException;
import com.server.EZY.exception.customError.exception.CustomForbiddenException;
import com.server.EZY.exception.customError.exception.CustomNotFoundException;
import com.server.EZY.exception.customError.exception.CustomUnauthorizedException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.exception.authenticationNumber.exception.InvalidAuthenticationNumberException;
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
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdviceImpl implements ExceptionAdvice{

    private final ExceptionResponseObjectUtil exceptionResponseObjectUtil;

//    // 알수없는 에러
//    @Override
//    public CommonResult defaultException(Exception ex){
//        log.error("=== 알 수 없는 애러 발생 ===", ex);
//        return exceptionResponseObjectUtil.getExceptionResponseObj(DEFAULT_EXCEPTION);
//    }

    /*** Custom Server Exception 시작 ***/
    @Override
    public CommonResult unauthorized(CustomUnauthorizedException ex) {
        log.debug("=== 401 Unauthorized Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_401_UNAUTHORIZED);
    }

    @Override
    public CommonResult forbiddenException(CustomForbiddenException ex) {
        log.debug("=== 403 Forbidden Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_403_FORBIDDEN);
    }

    @Override
    public CommonResult notFoundException(CustomNotFoundException ex) {
        log.debug("=== 404 NotFound Exception 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(CUSTOM_404_NOT_FOUND);
    }

    /*** UserException 시작 ***/
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
        usernameNotFoundExceptionResponseObj.setMassage(insertUsernameInExceptionMassage);

        return usernameNotFoundExceptionResponseObj;
    }

    @Override
    public CommonResult invalidAccessException(InvalidAccessException ex) {
        log.debug("=== InvalidAccessException 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(INVALID_ACCESS);
    }

    @Override
    public CommonResult invalidAuthenticationNumberException(InvalidAuthenticationNumberException ex) {
        log.debug("=== InvalidAuthenticationNumberException 발생 ===");
        return exceptionResponseObjectUtil.getExceptionResponseObj(INVALID_AUTHENTICATION_NUMBER);
    }

    @Override
    public CommonResult authenticationNumberTransferFailedException(AuthenticationNumberTransferFailedException ex) {
        log.debug("=== AuthenticationNumberTransferFailedException 발생");
        return exceptionResponseObjectUtil.getExceptionResponseObj(AUTHENTICATION_NUMBER_TRANSFER_FAILED);
    }
}
