package com.tc.service.impl;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.Resource;
import com.tc.db.repository.AuthorityRepository;
import com.tc.db.repository.AuthorityResourceRepository;
import com.tc.dto.Show;
import com.tc.dto.authority.QueryAuthority;
import com.tc.dto.authority.RemoveUser;
import com.tc.service.AuthorityService;
import com.tc.until.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyg
 * 权限服务的实现
 */
@Service
public class AuthorityServiceImpl extends AbstractBasicServiceImpl<Authority> implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityResourceRepository authorityResourceRepository;

    @Override
    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(Long id) {
        authorityRepository.delete(id);
        Authority authority = findOne(id);
        return authority == null;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(List<Long> ids) {
        int count = authorityRepository.deleteByIds(ids);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Authority update(Authority authority) {
        int count = authorityRepository.update(authority);
        if (count > 0){
            return findOne(authority.getId());
        }
        return authority;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Authority findOne(Long id) {
        return authorityRepository.findOne(id);
    }


    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public boolean isNullByName(String name) {
        Authority authority = authorityRepository.queryFirstByName(name);
        if (authority != null) { return false; }
        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Authority> findByQueryAuthority(QueryAuthority queryAuthority) {
        return authorityRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAuthority.initPredicates(queryAuthority,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Authority> findByAdmin(Long id, QueryAuthority queryAuthority) {
        return authorityRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAuthority.initPredicates(queryAuthority,root,query,cb);
            predicates.add(cb.equal(root.join(Authority.ADMIN_AUTHORITY).get(Admin.USER_ID),id));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryAuthority);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Authority> findByNotId(Long id) {
        List<AuthorityResource> queryAr = authorityResourceRepository.findAllByResourceId(id);
        if (ListUtils.isNotEmpty(queryAr)){
            return authorityRepository.findByIdNotIn(AuthorityResource.toAuthorityIds(queryAr));
        }else {
            return authorityRepository.findAll();
        }
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<Authority> findByIds(List<Long> ids) {
        return authorityRepository.findByIdIn(ids);
    }
}
