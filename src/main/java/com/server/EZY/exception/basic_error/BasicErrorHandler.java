package com.server.EZY.exception.basic_error;

import com.server.EZY.exception.response.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.*;

/**
 * SpringBoot에서 white label error를 handling 하기위한 controller
 * basic error(White label error)가 발생하면 JSON형태의 error메세지를 전달하는 BasicErrorController의 URL로 리다이렉트 된다.
 *
 * @author 정시원, 배태현
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class BasicErrorHandler implements ErrorController {

    public final static String BASIC_ERROR_BASE_URL = "/error/";

    public final static String UNAUTHORIZED_401_CODE_NAME = "unauthorized";
    public final static String FORBIDDEN_403_CODE_NAME = "forbidden";
    public final static String NOT_FOUND_404_CODE_NAME = "not-found";

    /**
     * BasicError(WhiteLabel) Error가 발생하면
     * BasicError에 대한 Response객체를 반환하는 CustomErrorController에 정의되어있는 url로 리다이렉트 합니다.
     *
     * @param request HttpServletRequest
     * @return BasicErrorController에 정의해놓은 url주소
     * @author 정시원, 배태현
     */
    @GetMapping("/error")
    public String getHandleError(HttpServletRequest request)  {

        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final int statusCode = Integer.parseInt(status.toString());

        final String IP = request.getRemoteAddr() != null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR");

        log.info("\n=== Basic Error(WhiteLabel Error) 발생 === \n" +
                "http status code : {}\n" +
                "url : {}\n" +
                "IP : {}"
                ,statusCode, request.getRequestURI(), IP
        );

        if(statusCode == UNAUTHORIZED.value())
            return BASIC_ERROR_BASE_URL + UNAUTHORIZED_401_CODE_NAME;
        if(statusCode == FORBIDDEN.value())
            return BASIC_ERROR_BASE_URL + FORBIDDEN_403_CODE_NAME;
        if(statusCode == NOT_FOUND.value())
            return BASIC_ERROR_BASE_URL + NOT_FOUND_404_CODE_NAME;

        throw new CustomException("basic error처리중 알 수 없는 에러가 발생해 error handling을 하지 못했습니다", INTERNAL_SERVER_ERROR);
    }

    /**
     * BasicError(WhiteLabel) Error가 발생하면
     * BasicError에 대한 Response객체를 반환하는 CustomErrorController에 정의되어있는 url로 리다이렉트 합니다.
     *
     * @param request HttpServletRequest
     * @return BasicErrorController에 정의해놓은 url주소
     * @author 정시원, 배태현
     */
    @PostMapping("/error")
    public String postHandleError(HttpServletRequest request)  {

        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final int statusCode = Integer.parseInt(status.toString());

        final String IP = request.getRemoteAddr() != null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR");

        log.info("\n=== Basic Error(WhiteLabel Error) 발생 === \n" +
                        "http status code : {}\n" +
                        "url : {}\n" +
                        "IP : {}"
                ,statusCode, request.getRequestURI(), IP
        );

        if(statusCode == UNAUTHORIZED.value())
            return BASIC_ERROR_BASE_URL + UNAUTHORIZED_401_CODE_NAME;
        if(statusCode == FORBIDDEN.value())
            return BASIC_ERROR_BASE_URL + FORBIDDEN_403_CODE_NAME;
        if(statusCode == NOT_FOUND.value())
            return BASIC_ERROR_BASE_URL + NOT_FOUND_404_CODE_NAME;

        throw new CustomException("basic error처리중 알 수 없는 에러가 발생해 error handling을 하지 못했습니다", INTERNAL_SERVER_ERROR);
    }

    /**
     * BasicError(WhiteLabel) Error가 발생하면
     * BasicError에 대한 Response객체를 반환하는 CustomErrorController에 정의되어있는 url로 리다이렉트 합니다.
     *
     * @param request HttpServletRequest
     * @return BasicErrorController에 정의해놓은 url주소
     * @author 정시원, 배태현
     */
    @PutMapping("/error")
    public String putHandleError(HttpServletRequest request)  {

        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final int statusCode = Integer.parseInt(status.toString());

        final String IP = request.getRemoteAddr() != null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR");

        log.info("\n=== Basic Error(WhiteLabel Error) 발생 === \n" +
                        "http status code : {}\n" +
                        "url : {}\n" +
                        "IP : {}"
                ,statusCode, request.getRequestURI(), IP
        );

        if(statusCode == UNAUTHORIZED.value())
            return BASIC_ERROR_BASE_URL + UNAUTHORIZED_401_CODE_NAME;
        if(statusCode == FORBIDDEN.value())
            return BASIC_ERROR_BASE_URL + FORBIDDEN_403_CODE_NAME;
        if(statusCode == NOT_FOUND.value())
            return BASIC_ERROR_BASE_URL + NOT_FOUND_404_CODE_NAME;

        throw new CustomException("basic error처리중 알 수 없는 에러가 발생해 error handling을 하지 못했습니다", INTERNAL_SERVER_ERROR);
    }

    /**
     * BasicError(WhiteLabel) Error가 발생하면
     * BasicError에 대한 Response객체를 반환하는 CustomErrorController에 정의되어있는 url로 리다이렉트 합니다.
     *
     * @param request HttpServletRequest
     * @return BasicErrorController에 정의해놓은 url주소
     * @author 정시원, 배태현
     */
    @DeleteMapping("/error")
    public String deleteHandleError(HttpServletRequest request)  {

        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final int statusCode = Integer.parseInt(status.toString());

        final String IP = request.getRemoteAddr() != null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR");

        log.info("\n=== Basic Error(WhiteLabel Error) 발생 === \n" +
                        "http status code : {}\n" +
                        "url : {}\n" +
                        "IP : {}"
                ,statusCode, request.getRequestURI(), IP
        );

        if(statusCode == UNAUTHORIZED.value())
            return BASIC_ERROR_BASE_URL + UNAUTHORIZED_401_CODE_NAME;
        if(statusCode == FORBIDDEN.value())
            return BASIC_ERROR_BASE_URL + FORBIDDEN_403_CODE_NAME;
        if(statusCode == NOT_FOUND.value())
            return BASIC_ERROR_BASE_URL + NOT_FOUND_404_CODE_NAME;

        throw new CustomException("basic error처리중 알 수 없는 에러가 발생해 error handling을 하지 못했습니다", INTERNAL_SERVER_ERROR);
    }
}
