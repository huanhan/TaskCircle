package com.tc.db.repository;

import com.tc.db.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 审核仓库
 * @author Cyg
 */
public interface AuditRepository extends JpaRepository<Audit,String>,JpaSpecificationExecutor<Audit> {
}
