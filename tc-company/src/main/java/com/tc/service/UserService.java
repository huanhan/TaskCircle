package com.tc.service;

import com.tc.db.entity.User;
import com.tc.db.enums.UserState;
import com.tc.dto.TimeScope;
import com.tc.dto.user.CashPledge;
import com.tc.dto.user.ModifyPassword;
import com.tc.dto.user.QueryUser;
import com.tc.until.PageRequest;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

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
     * @return
     */
    Boolean updateState();

    /**
     * 根据编号与状态获取
     * @param id
     * @param state
     * @return
     */
    User findByIdAndState(Long id, UserState state);

    /**
     * 更新用户的状态与管理员审核时间
     * @param id
     * @param state
     * @param now
     * @return
     */
    Boolean updateState(Long id, UserState state, Date now,Long me);

    /**
     * 更新用户状态与提交审核的时间
     * @param id
     * @param auditHunter
     * @param date
     * @return
     */
    Boolean updateStateAndAuditTime(Long id, UserState auditHunter, Date date);


    /**
     * 根据查询条件获取符合该查询条件的用户的数量
     * @param queryUser
     * @return
     */
    long countByQuery(QueryUser queryUser);

    /**
     * 根据编号获取数量
     * @param lIds
     * @return
     */
    long countByIds(List<Long> lIds);

    /**
     * 判断两个密码是否相同
     * @param password
     * @param oldPassword
     * @return
     */
    boolean hasPasswordEquals(String password, String oldPassword);

    /**
     * 修改用户密码
     * @param id
     * @param modifyPassword
     * @return
     */
    boolean updatePassword(Long id, ModifyPassword modifyPassword);

    /**
     * 修改用户头像
     * @param id
     * @param header
     * @return
     */
    boolean updateHeader(Long id, String header);

    /**
     * 获取普通用户的押金列表
     * @param scope
     * @return
     */
    List<CashPledge> findByCashPledgeAndUser(TimeScope scope);

    /**
     * 根据账户获取编号
     * @param account
     * @return
     */
    Long loadId(String account);

    /**
     * 根据账户获取
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据管理员编号获取该管理员审核记录数
     * @param me
     * @return
     */
    Integer countByAdmin(Long me);


    /**
     * 根据编号列表与分页信息获取用户数据
     * @param ids
     * @param page
     * @return
     */
    Page<User> findByIdsAndPage(List<Long> ids, PageRequest page);

}
