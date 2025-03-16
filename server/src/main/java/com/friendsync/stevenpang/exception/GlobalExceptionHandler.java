package com.friendsync.stevenpang.exception;

import com.friendsync.stevenpang.common.BaseResponse;
import com.friendsync.stevenpang.common.ErrorCode;
import com.friendsync.stevenpang.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，捕获代码所有异常
 * 集中消化与内部处理，
 * 不暴露服务器内部状态
 */
// Spring AOP切面，在调用方法前后额外进行封装处理
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        return ResultUtils.error(e.getCode(), e.getMessage(), "");
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");

    }


}
