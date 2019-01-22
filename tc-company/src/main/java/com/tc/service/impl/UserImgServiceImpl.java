package com.tc.service.impl;

import com.tc.db.entity.UserImg;
import com.tc.db.repository.UserImgRepository;
import com.tc.service.UserImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户图片服务的实现
 * @author Cyg
 */
@Service
public class UserImgServiceImpl extends AbstractBasicServiceImpl<UserImg> implements UserImgService {

    @Autowired
    private UserImgRepository userImgRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public long countByUserId(Long userId){
        return userImgRepository.countByUserId(userId);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserImg> findByUser(Long id) {
        return userImgRepository.findByUserId(id);
    }
}
