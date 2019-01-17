package com.tc.service;

import com.tc.db.entity.UserImg;

/**
 * 用户图片服务
 *
 * @author Cyg
 */
public interface UserImgService extends BasicService<UserImg> {
    long countByUserId(Long userId);
}
