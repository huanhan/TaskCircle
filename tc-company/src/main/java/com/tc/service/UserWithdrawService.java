package com.tc.service;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.WithdrawState;
import com.tc.dto.finance.QueryFinance;
import org.springframework.data.domain.Page;

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
     * 自动更新用户提现状态
     * @param state
     * @return
     */
    Boolean updateState(WithdrawState state);
}
