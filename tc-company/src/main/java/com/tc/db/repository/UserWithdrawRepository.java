package com.tc.db.repository;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.WithdrawState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

import java.sql.Timestamp;
import java.util.List;

/**
 * 用户提现与充值仓库
 * @author Cyg
 */
public interface UserWithdrawRepository extends JpaRepository<UserWithdraw,String>,JpaSpecificationExecutor<UserWithdraw> {


    /**
     * 跟新用户提现状态
     * @param state
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update UserWithdraw  u set u.state = :state, u.adminId = null, u.adminAuditTime = NULL where u.id = :id")
    int updateState(@Param("state") WithdrawState state,@Param("id") String id);

    /**
     * 更新用户提现状态，并将审核时间制空
     * @param ids
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update UserWithdraw t set t.state = :state, t.adminAuditTime = NULL, t.adminId = NULL where t.id in (:ids)")
    int updateState(List<String> ids, WithdrawState state);

    /**
     * 更新提现状态与审核时间
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update UserWithdraw t set t.state = :state, t.adminAuditTime = :time, t.adminId = :adminId where t.id = :id")
    int updateState(@Param("id") String id, @Param("state") WithdrawState state, @Param("time")Timestamp timestamp,@Param("adminId") Long adminId);

    /**
     * 更新用户状态，包括真实金额，内容，状态，和依据的用户编号
     * @param money 重新设置金额
     * @param realityMoney
     * @param context
     * @param state
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update UserWithdraw t set " +
            "t.money = :money, " +
            "t.realityMoney = :realityMoney, " +
            "t.context = :context, " +
            "t.auditPassTime = :passTime, " +
            "t.state = :state, " +
            "t.adminAuditTime = NULL, " +
            "t.adminId = NULL " +
            "where t.id = :id")
    int updateState(@Param("money") Float money,
                    @Param("realityMoney") Float realityMoney,
                    @Param("context") String context,
                    @Param("passTime") Timestamp auditPassTime,
                    @Param("state") WithdrawState state,
                    @Param("id") String id);


    /**
     * 根据猎刃任务编号与状态查询
     * @param uwId
     * @param state
     * @return
     */
    @Query(value = "select uw from UserWithdraw uw where uw.id = :id and uw.state = :state")
    UserWithdraw findByIdAndState(@Param("id") String uwId, @Param("state") WithdrawState state);

    /**
     * 获取当前管理员审核数量
     * @param id
     * @return
     */
    int countByAdminId(Long id);

    /**
     * 获取指定财务状态的数量
     * @param state
     * @return
     */
    int countByStateEquals(WithdrawState state);
}
