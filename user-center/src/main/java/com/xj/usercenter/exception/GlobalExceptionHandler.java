package com.xj.usercenter.exception;

import com.xj.usercenter.common.BaseResponse;
import com.xj.usercenter.common.ErrorCode;
import com.xj.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理
 * @Author 嘻精
 * @Date 2023/4/26 17:06
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 添加此注解表示此方法只捕获这个异常
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler( BusinessException e) {
        log.error("businessException" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
