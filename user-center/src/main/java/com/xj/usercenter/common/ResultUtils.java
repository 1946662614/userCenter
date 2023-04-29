package com.xj.usercenter.common;

/**
 * @ClassName ResultUtils
 * @Description 返回工具类，用于创建成功和失败的返回对象
 * @Author 嘻精
 * @Date 2023/4/25 21:01
 * @Version 1.0
 */

public class ResultUtils {
    
    /**
     * 创建成功返回对象
     * @param data 传入的返回类型
     * @return
     * @param <T>
     */
    public static<T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0,data,"ok");
    }
    
    /**
     * 创建错误返回对象
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode,null,"");
    }
    
    /**
     * 失败
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code,null, message, description);
    }
    /**
     * 创建错误返回对象
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(),null, errorCode.getMessage(), errorCode.getDescription());
    }
    
    /**
     * 创建错误返回对象
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,  String description) {
        return new BaseResponse<>(errorCode.getCode(),null, errorCode.getMessage(), description);
    }
}
