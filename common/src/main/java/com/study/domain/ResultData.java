package com.study.domain;

import com.alibaba.fastjson.JSON;
import com.study.constant.HttpStatus;
import lombok.Data;
import java.io.Serializable;

/**
 * 自定义统一响应结果返回类
 * @author yangz
 * @date 2022/12/3 - 11:55
 */
@Data
public class ResultData<T> implements Serializable {
    private boolean ret;
    private Integer code;
    private String errMsg;
    private T data;

    public ResultData() {
    }

    public ResultData(boolean ret, Integer code, String errMsg, T data) {
        this.ret = ret;
        this.code = code;
        this.errMsg = errMsg;
        this.data = data;
    }

    public static <T> ResultData<T> ok() {
        return new ResultData<>(true, HttpStatus.SUCCESS, null, null);
    }

    public static <T> ResultData<T> ok(T data) {
        return new ResultData<>(true, HttpStatus.SUCCESS, null, data);
    }

    public static <T> ResultData<T> error(String msg) {
        return new ResultData<>(false, HttpStatus.ERROR, msg, null);
    }

    public static <T> ResultData<T> error(Integer code,String msg) {
        return new ResultData<>(false, code, msg, null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
