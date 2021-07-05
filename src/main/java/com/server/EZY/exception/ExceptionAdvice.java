package com.server.EZY.exception;

import com.server.EZY.exception.user.exception.UserNotFoundException;
import com.server.EZY.response.ResponseService;
import com.server.EZY.response.result.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    // code 정보에 해당하는 메시지를 조회한다.
    private String getMessage(String code){
        return getMessage(code, null);
    }

    // code 정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args){
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * Exception 처리시 i18n애 정의 되어있는 exception code, msg를 가져온다.
     * @param code exception 식별 이름
     * @return CommonResult - failResult
     */
    private CommonResult getExceptionResponse(String code){
        return responseService.getFailResult(Integer.valueOf(getMessage(code + ".code")), getMessage(code + ".msg"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private CommonResult userNotFoundException(Exception ex){
        log.debug("userNotFoundException");
        return getExceptionResponse("user-not-found");
    }

}
