package com.tc.service;

import com.tc.db.entity.UserContact;

import java.util.List;

/**
 * 用户联系方式服务
 * @author Cyg
 */
public interface UserContactService extends BasicService<UserContact> {

    /**
     * 获取用户所有的联系方式
     * @param id
     * @return
     */
    List<UserContact> findByUser(Long id);


}
