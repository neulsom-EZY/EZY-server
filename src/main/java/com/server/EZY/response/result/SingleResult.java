package com.server.EZY.response.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SingleResult<T> extends CommonResult{
    private T data;

    public SingleResult(CommonResult commonResult, T data){
        super(commonResult);
        this.data = data;
    }
}
