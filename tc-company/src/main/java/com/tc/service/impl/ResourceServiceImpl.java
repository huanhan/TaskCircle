package com.tc.service.impl;

import com.tc.db.entity.Resource;
import com.tc.db.repository.ResourceRepository;
import com.tc.dto.resource.QueryResource;
import com.tc.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
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

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Resource> findByQuery(QueryResource queryResource) {

        return resourceRepository.findAll(
                (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (!StringUtils.isEmpty(queryResource.getName())){
                        predicates.add(cb.like(root.get(QueryResource.NAME),"%" + queryResource.getName() + "%"));
                    }
                    if (!StringUtils.isEmpty(queryResource.getClassName())){
                        predicates.add(cb.like(root.get(QueryResource.CLASSNAME),"%" + queryResource.getClassName() + "%"));
                    }
                    if (!StringUtils.isEmpty(queryResource.getInfo())){
                        predicates.add(cb.like(root.get(QueryResource.INFO),"%" + queryResource.getInfo() + "%"));
                    }
                    if (!StringUtils.isEmpty(queryResource.getMethod())){
                        predicates.add(cb.like(root.get(QueryResource.METHOD),"%" + queryResource.getMethod() + "%"));
                    }
                    if (!StringUtils.isEmpty(queryResource.getType())){
                        predicates.add(cb.equal(root.get(QueryResource.TYPE),queryResource.getType()));
                    }
                    if (queryResource.getBegin() != null || queryResource.getEnd() != null){
                        if (queryResource.getBegin() != null && queryResource.getEnd() != null){
                            predicates.add(cb.between(root.get(QueryResource.CREATE_TIME),queryResource.getBegin(),queryResource.getEnd()));
                        }else if (queryResource.getBegin() != null){
                            predicates.add(cb.greaterThan(root.get(QueryResource.CREATE_TIME),queryResource.getBegin()));
                        }else if (queryResource.getEnd() != null){
                            predicates.add(cb.lessThan(root.get(QueryResource.CREATE_TIME),queryResource.getEnd()));
                        }
                    }
                    return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
                },queryResource
        );
    }
}
