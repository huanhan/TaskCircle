package com.tc.service.impl;

import com.tc.service.BasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class BasicServiceImpl<T,ID extends Serializable> implements BasicService<T> {

    @Autowired
    private JpaRepository<T,ID> repository;

    @Override
    public T save(T t) {
        return repository.save(t);
    }


}
