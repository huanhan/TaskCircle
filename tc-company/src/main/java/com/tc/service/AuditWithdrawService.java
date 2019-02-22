package com.tc.service;

import com.tc.db.entity.AuditWithdraw;

import java.util.List;

/**
 * 审核提现服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AuditWithdrawService extends BasicService<AuditWithdraw> {
    /**
     * 根据提现编号查询
     * @param id
     * @return
     */
    List<AuditWithdraw> findByWithdrawId(String id);
}
