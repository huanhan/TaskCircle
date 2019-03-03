package com.tc.db.repository;

import com.tc.db.entity.AuditTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 任务审核仓库
 * @author Cyg
 */
public interface AuditTaskRepository extends JpaRepository<AuditTask,String>,JpaSpecificationExecutor<AuditTask> {

    /**
     * 获取任务的被审核记录
     * @param id
     * @return
     */
    List<AuditTask> findByTaskId(String id);

}
