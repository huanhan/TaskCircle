package com.tc.db.repository;

import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Cyg
 * 用户资源
 */
public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    User queryFirstByUsername(String username);


    /**
     * 更新用户基本信息
     * @param user
     * @return
     */
    @Modifying
    @Query("update User u set " +
            "u.name = CASE WHEN :#{#user.name} IS NULL THEN u.name ELSE :#{#user.name} END ," +
            "u.category = CASE WHEN :#{#user.category.toString()} IS NULL THEN u.category ELSE :#{#user.category.toString()} END ," +
            "u.name = CASE WHEN :#{#user.name} IS NULL THEN u.name ELSE :#{#user.name} END ," +
            "u.gender = CASE WHEN :#{#user.gender.toString()} IS NULL THEN u.gender ELSE :#{#user.gender.toString()} END ," +
            "u.idCard = CASE WHEN :#{#user.idCard} IS NULL THEN u.idCard ELSE :#{#user.idCard} END ," +
            "u.address = CASE WHEN :#{#user.address} IS NULL THEN u.address ELSE :#{#user.address} END ," +
            "u.school = CASE WHEN :#{#user.school} IS NULL THEN u.school ELSE :#{#user.school} END ," +
            "u.major = CASE WHEN :#{#user.major} IS NULL THEN u.major ELSE :#{#user.major} END ," +
            "u.interest = CASE WHEN :#{#user.interest} IS NULL THEN u.interest ELSE :#{#user.interest} END ," +
            "u.intro = CASE WHEN :#{#user.intro} IS NULL THEN u.intro ELSE :#{#user.intro} END ," +
            "u.height = CASE WHEN :#{#user.height} IS NULL THEN u.height ELSE :#{#user.height} END ," +
            "u.weight = CASE WHEN :#{#user.weight} IS NULL THEN u.weight ELSE :#{#user.weight} END ," +
            "u.birthday = CASE WHEN :#{#user.birthday} IS NULL THEN u.birthday ELSE :#{#user.birthday} END " +
            "WHERE u.id = :#{#user.id}"
    )
    int update(@Param("user") User user);

    /**
     * 更新用户状态
     * @param state
     * @param id
     * @return
     */
    @Modifying
    @Query("update User u set u.state = :state ," +
            "u.category = :category, " +
            "u.adminId = null, " +
            "u.adminAuditTime = null " +
            "where u.id = :id")
    int update(@Param("state") UserState state, @Param("category") UserCategory category, @Param("id") Long id);

    /**
     * 更新用户金额
     * @param money
     * @param id
     * @return
     */
    @Modifying
    @Query("update User u set u.money = :money where u.id = :id")
    int update(@Param("money") Float money, @Param("id") Long id);

    /**
     * 更新用户状态，并将审核时间制空
     * @param ids
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update User u set u.state = :state, u.adminAuditTime = NULL, u.adminId = NULL where u.id in (:ids)")
    int updateState(@Param("ids") List<Long> ids,@Param("state") UserState state);

    /**
     * 更新提现状态与用户提交审核时间
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update User u set u.state = :state, u.auditTime = :time where u.id = :id")
    int updateStateAndAuditTime(@Param("id") Long id, @Param("state") UserState state, @Param("time")Timestamp timestamp);

    /**
     * 更新提现状态与管理员开始审核时间
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update User u set u.state = :state, u.adminAuditTime = :time, u.adminId = :adminId where u.id = :id")
    int updateStateAndAdminAuditTime(@Param("id") Long id, @Param("state") UserState state, @Param("time")Timestamp timestamp,@Param("adminId") Long adminId);

    /**
     * 根据猎刃任务编号与状态查询
     * @param id
     * @param state
     * @return
     */
    @Query(value = "select u from User u where u.id = :id and u.state = :state")
    User findByIdAndState(@Param("id") Long id, @Param("state") UserState state);

    /**
     * 根据编号获取数量
     * @param lIds
     * @return
     */
    long countByIdIn(List<Long> lIds);

    /**
     * 修改用户密码
     * @param id
     * @param newPassword
     * @return
     */
    @Modifying
    @Query(value = "update User u set u.password = :password where u.id = :id")
    int updatePassword(@Param("id") Long id,@Param("password") String newPassword);

    /**
     * 修改用户头像
     * @param id
     * @param header
     * @return
     */
    @Modifying
    @Query(value = "update User u set u.headImg = :header where u.id = :id")
    int updateHeader(@Param("id") Long id,@Param("header") String header);

    /**
     * 获取用户编号
     * @param account
     * @return
     */
    @Query(value = "select u.id from User u where u.username = :account")
    Long loadId(@Param("account") String account);

    /**
     * 获取指定管理员的审核数目
     * @param id
     * @return
     */
    int countByAdminId(Long id);


    /**
     * 根据用户编号列表获取
     * @param ids
     * @return
     */
    List<User> findByIdIn(List<Long> ids);

    /**
     * 获取指定状态的用户数量
     * @param state
     * @return
     */
    int countByStateEquals(UserState state);
}
