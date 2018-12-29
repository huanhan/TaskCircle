package com.tc.service.impl;

import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.repository.AuthorityResourceRepository;
import com.tc.service.AuthorityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean deleteByResourceIds(List<Long> ids) {
        int count = authorityResourceRepository.deleteByResource_Id(ids);
        return count == ids.size();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<AuthorityResource> save(List<AuthorityResource> authorityResources) {
        return authorityResourceRepository.save(authorityResources);
    }


}
