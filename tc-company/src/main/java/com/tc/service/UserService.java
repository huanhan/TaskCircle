package com.tc.service;

import com.tc.db.entity.User;
import com.tc.db.enums.UserState;
import com.tc.dto.user.QueryUser;
import org.springframework.data.domain.Page;

/**
 * 用户服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
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

    /**
     * 根据查询条件获取用户信息
     * @param queryUser
     * @return
     */
    Page<User> findByQueryUser(QueryUser queryUser);

    /**
     * 自动更新用户状态
     * @param state
     * @return
     */
    Boolean updateState(UserState state);
}
