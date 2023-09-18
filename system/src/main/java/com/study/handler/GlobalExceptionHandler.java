package com.study.handler;

import com.study.domain.ResultData;
import com.study.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yangz
 * @date 2022/12/6 - 13:45
 * 全局异常处理类
 */
@RestControllerAdvice //捕获controller抛出的异常
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //1.处理全局异常
    @ExceptionHandler(Exception.class)
    public ResultData<String> error(Exception e) {
        logger.error("服务器异常:{}", e.getMessage());
        return ResultData.error(e.getMessage());
    }

    //2.处理指定异常(空指针)--如果有指定异常发生会进入其对应特定处理方法,并不会进入
    @ExceptionHandler(NullPointerException.class)
    public ResultData<String> error(NullPointerException e) {
        logger.error("服务器异常:{}", e.getMessage());
        return ResultData.error(e.getMessage());
    }

    //3.处理自定义异常
    @ExceptionHandler(BusinessException.class)
    public ResultData<String> error(BusinessException e) {
        logger.error("服务器异常:{}", e.getMsg());
        return ResultData.error(e.getMsg());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResultData<String> error(UsernameNotFoundException e) {
        logger.error("服务器异常:{}", e.getMessage());
        return ResultData.error(e.getMessage());
    }

}
