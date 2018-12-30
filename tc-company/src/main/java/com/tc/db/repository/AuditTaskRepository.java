package com.tc.db.repository;

import com.tc.db.entity.AuditTask;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 任务审核仓库
 * @author Cyg
 */
public interface AuditTaskRepository extends JpaRepository<AuditTask,String> {
}
