package com.tc.service.impl;

import com.tc.service.BasicService;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class AbstractBasicServiceImpl<T> implements BasicService<T> {

    @Override
    public T save(T t) {
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
    public boolean isNullByName(String name) {
        return false;
    }
}