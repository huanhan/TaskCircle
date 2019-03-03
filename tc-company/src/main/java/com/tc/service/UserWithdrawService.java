package com.tc.service;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.WithdrawState;
import com.tc.dto.TimeScope;
import com.tc.dto.finance.QueryFinance;
import org.springframework.data.domain.Page;

import javax.management.Query;
import java.util.Date;
import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserWithdrawService extends BasicService<UserWithdraw> {
    /**
     * 根据条件查询用户财务记录
     * @param queryFinance
     * @return
     */
    Page<UserWithdraw> findByQueryFinance(QueryFinance queryFinance);

    /**
     * 根据查询条件获取用户财务记录
     * @param queryFinance
     * @return
     */
    List<UserWithdraw> findByQueryFinanceNotPage(QueryFinance queryFinance);

    /**
     * 自动更新用户提现状态
     * @return
     */
    Boolean updateState();

    /**
     * 根据编号与状态获取提现信息
     * @param id 编号
     * @param state 状态
     * @return
     */
    UserWithdraw findByIdAndState(String id, WithdrawState state);

    /**
     * 更新任务状态与管理员审核时间
     * @param id
     * @param state
     * @param now
     * @return
     */
    Boolean updateState(String id, WithdrawState state, Date now, Long me);

    /**
     * 获取审核列表中的用户押金
     * @param id
     * @param scope
     * @return
     */
    Page<UserWithdraw> findByCashPledge(Long id, TimeScope scope);

    /**
     * 获取指定管理员的审核数量
     * @param me
     * @return
     */
    Integer countByAdmin(Long me);
}
