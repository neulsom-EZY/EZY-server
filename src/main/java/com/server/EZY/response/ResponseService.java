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
        SUCCESS(1, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        int code;
        String massage;

        CommonResponse(int code, String massage){
            this.code = code;
            this.massage = massage;
        }
    }

    /**
     * CommonResult setting method
     * @param result Client에게 반환할 CommonResult객체
     * @param commonResponse 요청의 성공/실패 여부 알려주는 CommonResponse
     * @return CommonResult - 성공/실패 응답 객체 반환
     * @author 정시원
     */
    private CommonResult setResponseResult(CommonResult result, CommonResponse commonResponse){
        result.setSuccess(commonResponse == CommonResponse.SUCCESS);
        result.setCode(commonResponse.getCode());
        result.setMassage(commonResponse.getMassage());
        return result;
    }

    /**
     * 요청 데이터가 없는 성공결과 객체 반환
     * @return CommonResult - 성공결과 객체
     * @author 정시원
     */
    public CommonResult getSuccessResult() {
        return setResponseResult(new CommonResult(), CommonResponse.SUCCESS);
    }

    /**
     * 단일건의 데이터가 있는 결과 객체 반환
     * @param data 단일 데이터
     * @param <T> 단일 데이터 타입
     * @return SingleResult - 단일건의 데이터가 있는 결과객체
     * @author 정시원
     */
    public <T> SingleResult<T> getSingleResult(T data){
        return new SingleResult<T>(getSuccessResult(), data);
    }


    /**
     * 다중값의 데이터가 있는 결과 객체 반환
     * @param list 다중 데이터
     * @param <T> 다중 데이터 타입
     * @return SingleResult - 다중값의 데이터가 있는 결과객체
     * @author 정시원
     */
    public <T> ListResult<T> getListResult(List<T> list){
        return new ListResult<T>(getSuccessResult(), list);
    }

    /**
     * 실패결과 객체 반환
     * @return CommonResult 실패결과 객체
     * @author 정시원
     */
    public CommonResult getFailResult() {
        return setResponseResult(new CommonResult(), CommonResponse.FAIL);
    }

    /**
     * 커스텀 실패결과 객체 반환
     * @param code 반환할 code
     * @param msg 반환할 message
     * @return CommonResult - 실패결과 객체
     * @author 정시원
     */
    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMassage(msg);
        return result;
    }
}