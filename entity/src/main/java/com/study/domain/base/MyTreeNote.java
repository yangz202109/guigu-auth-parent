package com.study.domain.base;

import java.util.List;

/**
 * @param <T> 自定义树通用接口
 */
public interface MyTreeNote<T extends MyTreeNote<T>> {
    T parent();

    void setChildren(List<T> children);

    List<T> getChildren();
}