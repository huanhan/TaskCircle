package com.tc.service;

import com.tc.db.entity.AuditHunterTask;

import java.util.List;

/**
 * 审核猎刃任务服务接口
 */
public interface AuditHunterTaskSerivce extends BasicService<AuditHunterTask> {
    /**
     * 获取指定猎刃任务的审核记录
     * @param id
     * @return
     */
    List<AuditHunterTask> findByHunterTaskId(String id);
}
