package com.tc.service;

public interface BasicService<T> {
    /**
     * 添加新记录
     * @param t
     * @return
     */
    T save(T t);

}
