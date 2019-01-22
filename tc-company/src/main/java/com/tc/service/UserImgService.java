package com.tc.service;

import com.tc.db.entity.UserImg;

import java.util.List;

/**
 * 用户图片服务
 *
 * @author Cyg
 */
public interface UserImgService extends BasicService<UserImg> {
    /**
     * 统计用户图片数量
     * @param userId
     * @return
     */
    long countByUserId(Long userId);

    /**
     * 获取用户图片资料
     * @param id
     * @return
     */
    List<UserImg> findByUser(Long id);
}
