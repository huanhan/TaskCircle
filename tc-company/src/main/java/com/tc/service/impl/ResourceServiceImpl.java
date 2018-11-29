package com.tc.service.impl;

import com.tc.db.entity.Resource;
import com.tc.db.repository.ResourceRepository;
import com.tc.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public List<Resource> findAll(Sort sort) {
        return resourceRepository.findAll(sort);
    }
}
