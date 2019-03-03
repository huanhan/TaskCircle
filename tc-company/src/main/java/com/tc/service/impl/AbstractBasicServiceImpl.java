package com.tc.service.impl;

import com.tc.dto.LongIds;
import com.tc.dto.StringIds;
import com.tc.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

public abstract class AbstractBasicServiceImpl<T> implements BasicService<T> {




    @Override
    public T save(T t) {
        return null;
    }

    @Override
    public List<T> save(List<T> ts) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public List<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<T> findByIds(List<Long> ids) {
        return null;
    }

    @Override
    public boolean isNullByName(String name) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public T update(T user) {
        return null;
    }

    @Override
    public boolean deleteByIds(List<Long> ids) {
        return false;
    }

    @Override
    public boolean deleteByIds(LongIds ids) {
        return false;
    }

    @Override
    public boolean deleteByIds(StringIds ids) {
        return false;
    }

    @Override
    public T findOne(Long id) {
        return null;
    }

    @Override
    public T findOne(String id) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Object redisGet(String key) {
        return null;
    }

    @Override
    public void redisSave(String key, Object value) {

    }
}
