package com.tc.db.repository;

import com.tc.db.entity.AuditWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 提现审核仓库
 * @author Cyg
 */
public interface AuditWithdrawRepository extends JpaRepository<AuditWithdraw,String>,JpaSpecificationExecutor<AuditWithdraw> {
    /**
     * 根据提现编号查询
     * @param id
     * @return
     */
    List<AuditWithdraw> findByWithdrawId(String id);
}
