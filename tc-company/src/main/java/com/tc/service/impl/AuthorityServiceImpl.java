package com.tc.service.impl;

import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;
import com.tc.db.repository.AuthorityRepository;
import com.tc.dto.Show;
import com.tc.dto.authority.QueryAuthority;
import com.tc.dto.authority.RemoveUser;
import com.tc.service.AuthorityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
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
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(queryAuthority.getAuthorityName())){
                predicates.add(cb.like(root.get("name"),"%" + queryAuthority.getAuthorityName() + "%"));
            }
            if (!StringUtils.isEmpty(queryAuthority.getInfo())){
                predicates.add(cb.like(root.get("info"),"%" + queryAuthority.getInfo() + "%"));
            }
            if (queryAuthority.getBegin() != null || queryAuthority.getEnd() != null){
                if (queryAuthority.getBegin() != null && queryAuthority.getEnd() != null){
                    predicates.add(cb.between(root.get("createTime"),queryAuthority.getBegin(),queryAuthority.getEnd()));
                }else if (queryAuthority.getBegin() != null){
                    predicates.add(cb.greaterThan(root.get("createTime"),queryAuthority.getBegin()));
                }else if (queryAuthority.getEnd() != null){
                    predicates.add(cb.lessThan(root.get("createTime"),queryAuthority.getEnd()));
                }
            }
            if (!StringUtils.isEmpty(queryAuthority.getUsername())){
                predicates.add(cb.equal(root.get("admin").get("user").get("username"),queryAuthority.getUsername()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
    }


}
