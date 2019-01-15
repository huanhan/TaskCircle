package com.tc.db.repository;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.WithdrawState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserWithdrawRepository extends JpaRepository<UserWithdraw,String>,JpaSpecificationExecutor<UserWithdraw> {


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
}
