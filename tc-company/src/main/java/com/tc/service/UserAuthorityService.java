package com.tc.service;

import com.tc.db.entity.UserAuthority;
import com.tc.dto.authority.RemoveUser;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserAuthorityService extends BasicService<UserAuthority> {

    /**
     * 根据权限编号获取对应的使用者
     * @param authorityId
     * @return
     */
    List<UserAuthority> findByAuthorityId(Long authorityId);

    /**
     * 删除记录，根据编号组与指定ID
     * @param ids
     * @return
     */
    boolean deleteByIds(RemoveUser ids);
}
