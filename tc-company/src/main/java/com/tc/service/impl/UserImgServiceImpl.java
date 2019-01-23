package com.tc.service.impl;

import com.tc.db.entity.UserImg;
import com.tc.db.entity.pk.UserImgPK;
import com.tc.db.repository.UserImgRepository;
import com.tc.exception.DBException;
import com.tc.service.UserImgService;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        return userImgRepository.countByUserId(userId,new Sort(Sort.Direction.DESC,UserImg.IMG_NAME));
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<UserImg> findByUser(Long id) {
        return userImgRepository.findByUserId(id);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public UserImg findOne(UserImgPK userImgPK) {
        return userImgRepository.findOne(userImgPK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(UserImgPK userImgPK) {
        try {
            userImgRepository.delete(userImgPK);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public UserImg save(UserImg userImg) {
        UserImg query = userImgRepository.findOne(new UserImgPK(userImg.getUserId(),userImg.getImgName()));
        if (query != null){
            throw new DBException(StringResourceCenter.VALIDATOR_ADD_ID_FAILED);
        }
        return userImgRepository.save(userImg);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public UserImg update(UserImg userImg) {
        int count = userImgRepository.update(userImg.getUrlLocation(),userImg.getImgName(),userImg.getUserId());
        if (count > 0){
            return userImgRepository.findOne(new UserImgPK(userImg.getUserId(),userImg.getImgName()));
        }
        return null;
    }
}
