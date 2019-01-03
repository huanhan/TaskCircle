package com.tc.service.impl;

import com.tc.db.entity.Resource;
import com.tc.db.repository.ResourceRepository;
import com.tc.dto.resource.QueryResource;
import com.tc.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyg
 *
 * url资源服务的具体实现
 *
 */
@Service
public class ResourceServiceImpl extends AbstractBasicServiceImpl<Resource> implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<Resource> save(List<Resource> resources) {
        return resourceRepository.save(resources);
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

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Resource> findAll(Sort sort) {
        return resourceRepository.findAll(sort);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Resource> findByIds(List<Long> ids) {
        return resourceRepository.findByIdIn(ids);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
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

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Resource findOne(Long id) {
        return resourceRepository.findOne(id);
    }

    @Override
    public List<Resource> findByQuery(QueryResource queryResource) {
        return resourceRepository.findByQuery(queryResource,queryResource.toPageRequest()).getContent();
    }
}
