package com.server.EZY.response;

import com.server.EZY.response.result.CommonResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResponseServiceTest {

    @Autowired ResponseService responseService;

    final String SUCCESS_MSG = ResponseService.CommonResponse.SUCCESS.massage;
    final int SUCCESS_CODE = ResponseService.CommonResponse.SUCCESS.code;

    final String FAIL_MSG = ResponseService.CommonResponse.FAIL.massage;
    final int FAIL_CODE = ResponseService.CommonResponse.FAIL.code;

    @Test @DisplayName("ResponseService.CommonResponse 성공시 message, code 검증")
    void CommonResponse_성공_message_code_검증(){
        assertEquals("성공하였습니다.", SUCCESS_MSG);
        assertEquals(1, SUCCESS_CODE);
    }

    @Test @DisplayName("\"ResponseService.CommonResponse 실패시 message, code 검증")
    void CommonResponse_실패_message_code_검증(){
        assertEquals("실패하였습니다.", FAIL_MSG);
        assertEquals(1, FAIL_CODE);
    }
}