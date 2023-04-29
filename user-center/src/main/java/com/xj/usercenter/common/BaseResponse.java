package com.xj.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BaseResponse
 * @Description 通用返回类
 * @Author 嘻精
 * @Date 2023/4/25 20:54
 * @Version 1.0
 */
@Data
// 定义泛型T可以让任意类型传入类
public class BaseResponse<T> implements Serializable {
    
    private int code;
    
    private T data;
    
    private String message;
    
    private String description;
    
    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }
    
    public BaseResponse(int code, T data, String message) {
        this(code,data,message,"");
    }
    
    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }
    
    public BaseResponse(ErrorCode errorCode, T data, String description) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
