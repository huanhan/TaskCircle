package com.tc.db.repository;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.WithdrawState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query(value = "update UserWithdraw  u set u.state = :state where u.id = :id")
    int updateState(@Param("state") WithdrawState state,@Param("id") String id);

    /**
     * 更新用户提现状态，并将审核时间制空
     * @param ids
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update UserWithdraw t set t.state = :state, t.adminAuditTime = NULL where t.id in (:ids)")
    int updateState(List<String> ids, WithdrawState state);

    /**
     * 更新提现状态与审核时间
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update UserWithdraw t set t.state = :state, t.adminAuditTime = :time where t.id = :id")
    int updateState(@Param("id") String id, @Param("state") WithdrawState state, @Param("time")Timestamp timestamp);

    /**
     * 根据猎刃任务编号与状态查询
     * @param uwId
     * @param state
     * @return
     */
    @Query(value = "select uw from UserWithdraw uw where uw.id = :id and uw.state = :state")
    UserWithdraw findByIdAndState(@Param("id") String uwId, @Param("state") WithdrawState state);
}
