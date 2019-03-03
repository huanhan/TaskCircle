package com.tc.db.repository;

import com.tc.db.entity.AuditHunterTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 审核猎刃任务仓库
 * @author Cyg
 */
public interface AuditHunterTaskRepository extends JpaRepository<AuditHunterTask,String>,JpaSpecificationExecutor<AuditHunterTask> {


    /**
     * 根据猎刃任务编号获取审核记录
     * @param id
     * @return
     */
    List<AuditHunterTask> findByHunterTaskId(String id);


}
