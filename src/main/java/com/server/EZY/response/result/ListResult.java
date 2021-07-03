package com.server.EZY.response.result;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ListResult<T> extends CommonResult{
    private List<T> list;
}
