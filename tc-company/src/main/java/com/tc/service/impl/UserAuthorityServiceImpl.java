package com.tc.service.impl;

import com.tc.db.entity.UserAuthority;
import com.tc.db.enums.UserCategory;
import com.tc.db.repository.UserAuthorityRepository;
import com.tc.dto.StringIds;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.authority.RemoveUser;
import com.tc.service.UserAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户与权限关系服务的实现
 * @author Cyg
 */
@Service
public class UserAuthorityServiceImpl extends AbstractBasicServiceImpl<UserAuthority> implements UserAuthorityService {
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserAuthority> findByAuthorityId(Long authorityId) {
        return userAuthorityRepository.findByAuthorityIdEquals(authorityId);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserAuthority> findAll() {
        return userAuthorityRepository.findAll(new Sort(Sort.Direction.DESC,UserAuthority.CATEGORY));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(RemoveUser ids) {
        return userAuthorityRepository.deleteByCategoryIsInAndAuthorityIdEquals(ids.getIds(),ids.getId()) == ids.getIds().size();
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserAuthority> findByUserCategory(UserCategory userCategory) {
        return userAuthorityRepository.findByCategoryEquals(userCategory);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<UserAuthority> save(List<UserAuthority> userAuthorities) {
        return userAuthorityRepository.save(userAuthorities);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByAuthorityIds(List<Long> ids,UserCategory userCategory) {
        int count = userAuthorityRepository.deleteByAuthorityIdIsInAndCategoryEquals(ids,userCategory);
        return count == ids.size();
    }



}
