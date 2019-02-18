package com.tc.service.impl;

import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.repository.AuthorityResourceRepository;
import com.tc.dto.LongIds;
import com.tc.dto.authority.QueryAR;
import com.tc.exception.DBException;
import com.tc.service.AuthorityResourceService;
import com.tc.until.ListUtils;
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
            if (!StringUtils.isEmpty(queryAR.getAuthorityInfo())){
                predicates.add(cb.like(root.get(AuthorityResource.AUTHORITY).get(Authority.INFO),"%" + queryAR.getResourceName() + "%"));
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
    public void saveNewsAndRemoveOlds(List<AuthorityResource> inAdd, List<Long> longs, Long aid) {

        if (ListUtils.isEmpty(inAdd) && ListUtils.isEmpty(longs)){
            throw new DBException("无意义的操作");
        }

        boolean isSuccess = true;
        String msg;

        if (ListUtils.isNotEmpty(inAdd)){
            List<AuthorityResource> result = authorityResourceRepository.save(inAdd);
            isSuccess = ListUtils.isNotEmpty(result) && result.size() == inAdd.size();
        }

        if (isSuccess) {

            if (ListUtils.isNotEmpty(longs)) {
                isSuccess = authorityResourceRepository.deleteByResourceIdIsInAndAuthorityIdEquals(longs, aid) == longs.size();

                if (isSuccess){
                    return;
                }else {
                    msg = "删除旧的资源错误";
                }

            }else {
                return;
            }

        }else {
            msg = "添加新的资源错误";
        }

        throw new DBException(msg);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AuthorityResource> findByAuthorityIds(List<Long> longs) {
        return authorityResourceRepository.findAllByAuthorityIdIn(longs);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<AuthorityResource> save(List<AuthorityResource> authorityResources) {
        return authorityResourceRepository.save(authorityResources);
    }


}
