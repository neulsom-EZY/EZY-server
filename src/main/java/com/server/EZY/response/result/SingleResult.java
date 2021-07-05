package com.server.EZY.response.result;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SingleResult<T> extends CommonResult{
    private T data;

    public SingleResult(CommonResult commonResult, T data){
        super(commonResult);
        this.data = data;
    }
}
