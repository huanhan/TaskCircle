package com.tc.service.impl;

import com.tc.db.entity.UserAuthority;
import com.tc.db.repository.UserAuthorityRepository;
import com.tc.dto.StringIds;
import com.tc.service.UserAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户与权限关系服务的实现
 * @author Cyg
 */
@Service
public class UserAuthorityServiceImpl extends AbstractBasicServiceImpl<UserAuthority> implements UserAuthorityService {
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;


    @Override
    public List<UserAuthority> findByAuthorityId(Long authorityId) {
        return userAuthorityRepository.findByAuthorityIdEquals(authorityId);
    }


    @Override
    public boolean deleteByIds(StringIds ids) {
        return userAuthorityRepository.deleteByCategoryIsInAndAuthorityIdEquals(ids.getIds(),ids.getId()) == ids.getIds().size();
    }
}
