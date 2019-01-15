package com.tc.db.repository;

import com.tc.db.entity.AuditWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 提现审核仓库
 * @author Cyg
 */
public interface AuditWithdrawRepository extends JpaRepository<AuditWithdraw,String>,JpaSpecificationExecutor<AuditWithdraw> {
}
