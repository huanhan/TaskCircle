package com.tc.service.impl;

import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.repository.AuthorityResourceRepository;
import com.tc.dto.LongIds;
import com.tc.dto.authority.QueryAR;
import com.tc.service.AuthorityResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyg
 */
@Service
public class AuthorityResourceServiceImpl extends AbstractBasicServiceImpl<AuthorityResource> implements AuthorityResourceService {

    @Autowired
    private AuthorityResourceRepository authorityResourceRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AuthorityResource> findAll() {
        return authorityResourceRepository.findAll();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AuthorityResource> findByAuthorityID(Long authorityID){
        return authorityResourceRepository.findAllByAuthority(new Authority(authorityID));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByResourceIds(List<Long> ids,Long authorityId) {
        int count = authorityResourceRepository.deleteByResourceIdIsInAndAuthorityIdEquals(ids,authorityId);
        return count == ids.size();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(LongIds ids) {
        return authorityResourceRepository.deleteByAuthorityIdIsInAndResourceIdEquals(ids.getIds(),ids.getId()) == ids.getIds().size();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<AuthorityResource> findByQuery(QueryAR queryAR) {

        return authorityResourceRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (queryAR.getAuthorityId() != null && queryAR.getAuthorityId() > 0){
                predicates.add(cb.equal(root.get("authorityId"),queryAR.getAuthorityId()));
            }
            if (queryAR.getResourceId() != null && queryAR.getResourceId() > 0){
                predicates.add(cb.equal(root.get("resourceId"),queryAR.getResourceId()));
            }
            if (!StringUtils.isEmpty(queryAR.getAuthorityName())){
                predicates.add(cb.like(root.get("authority").get("name"),"%" + queryAR.getAuthorityName() + "%"));
            }
            if (!StringUtils.isEmpty(queryAR.getResourceName())){
                predicates.add(cb.like(root.get("resource").get("name"),"%" + queryAR.getResourceName() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

        },queryAR);

    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AuthorityResource> findByKeys(List<Long> resources, List<Long> authorities) {
        return authorityResourceRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!resources.isEmpty()){
                predicates.add(root.get("resourceId").in(resources));
            }
            if (!authorities.isEmpty()){
                predicates.add(root.get("authorityId").in(authorities));
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();

        });
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<AuthorityResource> save(List<AuthorityResource> authorityResources) {
        return authorityResourceRepository.save(authorityResources);
    }


}
