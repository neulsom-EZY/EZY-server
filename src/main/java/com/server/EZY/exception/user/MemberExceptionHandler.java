package com.server.EZY.exception.user;

import com.server.EZY.exception.user.exception.*;
import com.server.EZY.response.result.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface MemberExceptionHandler {

    String MEMBER_NOT_FOUND = "member-not-found";
    String MEMBER_ALREADY_EXIST = "member-already-exist";
    String INVALID_ACCESS = "invalid-access";
    String USERNAME_NOT_FOUND = "username-not-found";
    String NOT_CORRECT_PASSWORD = "not-correct-password";
    String MEMBER_INFORMATION_CHECK_AGAIN = "member-information-check-again";

    // 회원를 찾을 수 없습니다.
    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult memberNotFoundException(MemberNotFoundException ex);

    // 해당 회원은 이미 존재합니다.
    @ExceptionHandler(MemberAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    CommonResult memberAlreadyExistException(MemberAlreadyExistException ex);

    // 해당 유저이름으로 맴버를 찾을 수 없습니다.
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    CommonResult usernameNotFoundException(UsernameNotFoundException ex);

    // 잘못된 접근입니다.
    @ExceptionHandler(InvalidAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    CommonResult invalidAccessException(InvalidAccessException ex);

    // 비밀번호가 올바르지 않습니다.
    @ExceptionHandler(NotCorrectPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult notCorrectPassword(NotCorrectPasswordException ex);

    // 변경하려는 회원의 정보를 다시 확인해주세요
    @ExceptionHandler(MemberInformationCheckAgainException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    CommonResult memberInformationCheckAgain(MemberInformationCheckAgainException ex);
}
