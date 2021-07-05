package com.server.EZY.response;

import com.server.EZY.response.result.CommonResult;
import com.server.EZY.response.result.ListResult;
import com.server.EZY.response.result.SingleResult;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Getter
    public enum CommonResponse{
        SUCCESS(200, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        int code;
        String massage;

        CommonResponse(int code, String massage){
            this.code = code;
            this.massage = massage;
        }
    }

    public CommonResult getSuccessResult() {
        return setSuccessResult(new CommonResult());
    }

    private CommonResult setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMassage(CommonResponse.SUCCESS.getMassage());

        return result;
    }

    public <T> SingleResult<T> getSingleResult(T data){
        return new SingleResult<T>(getSuccessResult(), data);
    }

    public <T> ListResult<T> getListResult(List<T> list){
        return new ListResult<T>(getSuccessResult(), list);
    }

    // 실패 결과만 처리하는 메소드
    public CommonResult getFailResult() {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMassage(CommonResponse.FAIL.getMassage());
        return result;
    }

    //getFailResult 메소드가 code, msg를 받을수 있도록 수정
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMassage(msg);
        return result;
    }
}