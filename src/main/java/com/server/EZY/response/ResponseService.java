package com.server.EZY.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.EZY.response.result.CommonResult;
import com.server.EZY.response.result.ListResult;
import com.server.EZY.response.result.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {

    public final ObjectMapper mapper;

    public enum CommonResponse{
        SUCCESS(200, "성공하였습니다"),
        FAIL(-1, "실패하였습니다");

        int code;
        String msg;

        CommonResponse(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode(){
            return code;
        }

        public String getMsg(){
            return msg;
        }
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>(data);
        setSuccessResult(result);
        return result;
    }
    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>(list);
        setSuccessResult(result);
        return result;
    }
    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }
    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult() {
        return CommonResult.builder()
                .success(false)
                .code(CommonResponse.FAIL.getCode())
                .msg(CommonResponse.FAIL.getMsg())
                .build();
    }
    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.updateCommonResult(
                true,
                CommonResponse.SUCCESS.getCode(),
                CommonResponse.SUCCESS.getMsg()
        );
    }
    //getFailResult 메소드가 code, msg를 받을수 있도록 수정
    public CommonResult getFailResult(int code, String msg) {
        return CommonResult.builder()
                .success(false)
                .code(code)
                .msg(msg)
                .build();
    }

    public String getFailResultConvertString(int code, String msg) throws JsonProcessingException {
        return mapper.writeValueAsString(getFailResult(code, msg));
    }

}
