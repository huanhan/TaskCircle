package com.tc.service.impl;

import com.tc.db.entity.Resource;
import com.tc.db.repository.ResourceRepository;
import com.tc.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceServiceImpl extends AbstractBasicServiceImpl<Resource> implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Resource update(Resource resource) {
        int count = resourceRepository.update(resource);
        if (count > 0){
            return findOne(resource.getId());
        }
        return resource;
    }

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public List<Resource> findAll(Sort sort) {
        return resourceRepository.findAll(sort);
    }

    @Override
    public boolean isNullByName(String name) {
        Resource resource = resourceRepository.queryFirstByName(name);
        if (resource != null) { return false; }
        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(Long id) {
        resourceRepository.delete(id);
        Resource resource = findOne(id);
        return resource == null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(List<Long> ids) {
        int count = resourceRepository.deleteByIds(ids);
        return count == ids.size();
    }

    @Override
    public Resource findOne(Long id) {
        return resourceRepository.findOne(id);
    }
}
