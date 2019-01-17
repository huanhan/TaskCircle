package com.tc.service.impl;

import com.tc.db.entity.UserImg;
import com.tc.db.repository.UserImgRepository;
import com.tc.service.UserImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户图片服务的实现
 * @author Cyg
 */
@Service
public class UserImgServiceImpl extends AbstractBasicServiceImpl<UserImg> implements UserImgService {

    @Autowired
    private UserImgRepository userImgRepository;

    @Override
    public long countByUserId(Long userId){
        return userImgRepository.countByUserId(userId);
    }
}
