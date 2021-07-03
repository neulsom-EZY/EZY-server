package com.server.EZY.response.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
public class CommonResult {
    @ApiModelProperty(value = "응답 성공여부")
    private boolean success;

    @ApiModelProperty(value = "응답 코드 번호")
    private int code;

    @ApiModelProperty(value = "응답 메시지")
    private String msg;

    public void updateCommonResult(boolean success, int code, String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
}
