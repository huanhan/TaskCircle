package com.tc.service;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface BasicService<T> {
    /**
     * 添加新记录
     * @param t
     * @return
     */
    T save(T t);

    /**
     * 获取表中所有记录
     * @return
     */
    List<T> findAll();

    /**
     * 获取列表中所有记录
     * @param sort 排序规则
     * @return
     */
    List<T> findAll(Sort sort);

}
