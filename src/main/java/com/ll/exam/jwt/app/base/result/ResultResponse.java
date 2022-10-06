package com.ll.exam.jwt.app.base.result;

import lombok.Getter;

@Getter
public class ResultResponse<T> {
    private String code;
    private String message;
    private T data;

    public ResultResponse(String resultCode,String message, T data) {
        this.code = resultCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultResponse<T> of(String resultCode,String message, T data) {
        return new ResultResponse<>(resultCode,message, data);
    }

    public static <T> ResultResponse<T> of(String resultCode, String message) {
        return new ResultResponse<>(resultCode,message,null);
    }
}
