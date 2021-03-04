package com.security.exception;

import com.security.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 * @author fhx
 * @date 2018年12月13日
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(BusinessException.class)
        public Result<?> businessExceptionHandler(BusinessException businessException) {
                log.warn("get error", businessException.getMessage());
                return Result.failure(businessException.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public Result<?> systemExceptionHandler(Exception excepiton) {
                log.warn("got error", excepiton.getMessage());
                return Result.failure("服务器忙，请稍后再试！");
        }
}