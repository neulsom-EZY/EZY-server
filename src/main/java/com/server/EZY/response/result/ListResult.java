package com.server.EZY.response.result;

import java.util.List;

public class ListResult<T> extends CommonResult{
    private List<T> list;

    public ListResult(CommonResult commonResult, List<T> list){
        super(commonResult);
        this.list = list;
    }

}
