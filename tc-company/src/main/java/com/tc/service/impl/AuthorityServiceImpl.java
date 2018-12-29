package com.tc.service.impl;

import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;
import com.tc.db.repository.AuthorityRepository;
import com.tc.dto.Show;
import com.tc.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return count == ids.size();
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
        Authority authority = authorityRepository.findOne(id);

        return authority;
    }


    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public boolean isNullByName(String name) {
        Authority authority = authorityRepository.queryFirstByName(name);
        if (authority != null) { return false; }
        return true;
    }
}
