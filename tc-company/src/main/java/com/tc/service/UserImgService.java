package com.tc.service;

import com.tc.db.entity.UserImg;

/**
 * 评论仓库
 *
 * @author Cyg
 */
public interface UserImgService extends BasicService<UserImg> {
    long countByUserId(Long userId);
}
