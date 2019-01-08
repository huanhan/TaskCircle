package com.tc.service;

import com.tc.dto.LongIds;
import com.tc.dto.StringIds;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 公用的一些服务接口
 * @author Cyg
 */
public interface BasicService<T> {
    /**
     * 添加新记录
     * @param t
     * @return
     */
    T save(T t);

    /**
     * 添加列表中的所有新记录
     * @param ts
     * @return
     */
    List<T> save(List<T> ts);

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

    /**
     * 获取分页记录
     * @param pageable
     * @return
     */
    List<T> findAll(Pageable pageable);

    /**
     * 根据编号列表查询
     * @param ids
     * @return
     */
    List<T> findByIds(List<Long> ids);


    /**
     * 获取记录详情
     * @param id
     * @return
     */
    T findOne(Long id);

    /**
     * 获取记录详情
     * @param id
     * @return
     */
    T findOne(String id);

    /**
     * 是否已存在name
     * @param name
     * @return
     */
    boolean isNullByName(String name);

    /**
     * 删除记录，根据编号
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 删除记录，根据编号组
     * @param ids
     * @return
     */
    boolean deleteByIds(List<Long> ids);

    /**
     * 删除记录，根据编号组与指定ID
     * @param ids
     * @return
     */
    boolean deleteByIds(LongIds ids);


    /**
     * 更新用户信息
     * @param t
     * @return
     */
    T update(T t);

    /**
     * 删除记录，根据编号组与指定ID
     * @param ids
     * @return
     */
    boolean deleteByIds(StringIds ids);
}
