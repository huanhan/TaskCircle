package com.tc.service.impl;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.repository.AdminAuthorityRepository;
import com.tc.dto.LongIds;
import com.tc.service.AdminAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员与权限关系服务的实现
 * @author Cyg
 */
@Service
public class AdminAuthorityServiceImpl extends AbstractBasicServiceImpl<AdminAuthority> implements AdminAuthorityService {

    @Autowired
    private AdminAuthorityRepository adminAuthorityRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(LongIds ids) {
        return adminAuthorityRepository.deleteByUserIdIsInAndAuthorityIdEquals(ids.getIds(),ids.getId()) == ids.getIds().size();
    }
}
