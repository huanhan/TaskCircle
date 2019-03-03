package com.tc.service;

import com.tc.db.entity.AuditTask;

import java.util.List;

/**
 * 审核任务服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AuditTaskService extends BasicService<AuditTask> {

    /**
     * 根据任务编号获取任务审核记录
     * @param id
     * @return
     */
    List<AuditTask> findByTaskId(String id);
}
