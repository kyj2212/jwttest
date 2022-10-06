package com.ll.exam.jwt.app.base.result;

import lombok.Getter;

@Getter
public class ResultResponse {
    private String code;
    private String message;
    private Object data;

    public ResultResponse(String resultCode,String message, Object data) {
        this.code = resultCode;
        this.message = message;
        this.data = data;
    }

    public static ResultResponse of(String resultCode,String message, Object data) {
        return new ResultResponse(resultCode,message, data);
    }

    public static ResultResponse of(String resultCode, String message) {
        return new ResultResponse(resultCode,message,"");
    }
}
