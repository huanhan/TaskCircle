package com.tc.service;

import com.tc.db.entity.User;

public interface UserService extends BasicService<User> {

    /**
     * 判断是否存在该用户名的用户
     * @param username
     * @return
     */
    boolean isNullByUsername(String username);

    /**
     * 根据用户账户获取用户信息
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 根据用户名获取对应的编号
     * @param username
     * @return
     */
    Long getIdByUsername(String username);
}
