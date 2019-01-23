package com.tc.service.impl;

import com.tc.db.entity.UserContact;
import com.tc.db.entity.pk.UserContactPK;
import com.tc.db.repository.UserContactRepository;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.UserContactService;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        return userContactRepository.findByUserId(id,new Sort(Sort.Direction.DESC,UserContact.CONTACT_NAME));
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public UserContact findOne(UserContactPK userContactPK) {
        return userContactRepository.findOne(userContactPK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(UserContactPK userContactPK) {
        try {
            userContactRepository.delete(userContactPK);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public UserContact save(UserContact userContact) {
        UserContact query = userContactRepository.findOne(new UserContactPK(userContact.getUserId(),userContact.getContactName()));
        if(query != null){
            throw new ValidException(StringResourceCenter.DB_INSERT_FAILED + "，存在在同类型的结果");
        }
        return userContactRepository.save(userContact);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public UserContact update(UserContact userContact) {
        int count = userContactRepository.update(userContact.getUserId(),userContact.getContactName(),userContact.getContact());
        if (count > 0){
            return userContactRepository.findOne(new UserContactPK(userContact.getUserId(),userContact.getContactName()));
        }
        return null;
    }
}
