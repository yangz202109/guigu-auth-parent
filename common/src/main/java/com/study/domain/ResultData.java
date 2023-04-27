package com.study.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import java.io.Serializable;
/**
 * @author yangz
 * @date 2022/12/3 - 11:55
 */
@Data
public class ResultData<T> implements Serializable {
    private boolean ret;
    private Long code;
    private String errMsg;
    private T data;

    public ResultData() {
    }

    public ResultData(boolean ret, Long code, String errMsg, T data) {
        this.ret = ret;
        this.code = code;
        this.errMsg = errMsg;
        this.data = data;
    }

    public static <T> ResultData<T> ok() {
        return new ResultData<>(true, 200L, null, null);
    }

    public static <T> ResultData<T> ok(T data) {
        return new ResultData<>(true, 200L, null, data);
    }

    public static <T> ResultData<T> error(String msg) {
        return new ResultData<>(false, 500L, msg, null);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
