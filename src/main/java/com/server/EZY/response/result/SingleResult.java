package com.server.EZY.response.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SingleResult<T> extends CommonResult{
    private T data;
}
