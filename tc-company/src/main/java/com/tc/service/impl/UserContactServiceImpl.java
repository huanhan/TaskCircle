package com.tc.service.impl;

import com.tc.db.entity.UserContact;
import com.tc.db.repository.UserContactRepository;
import com.tc.service.UserContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户联系方式服务的实现
 * @author Cyg
 */
@Service
public class UserContactServiceImpl extends AbstractBasicServiceImpl<UserContact> implements UserContactService {


    @Autowired
    private UserContactRepository userContactRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserContact> findByUser(Long id) {
        return userContactRepository.findByUserId(id);
    }
}
