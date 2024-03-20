package com.study.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yangz
 * @date 2022/12/6 - 14:23
 * 自定义异常类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException{
    private Integer code;
    private String msg;
}
