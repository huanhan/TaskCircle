package com.tc.service.impl;

import com.tc.db.entity.AuditHunterTask;
import com.tc.db.repository.AuditHunterTaskRepository;
import com.tc.service.AuditHunterTaskSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 审核猎刃任务服务的实现
 * @author Cyg
 */
@Service
public class AuditHunterTaskServiceImpl extends AbstractBasicServiceImpl<AuditHunterTask> implements AuditHunterTaskSerivce {

    @Autowired
    private AuditHunterTaskRepository auditHunterTaskRepository;


    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<AuditHunterTask> findByHunterTaskId(String id) {
        return auditHunterTaskRepository.findByHunterTaskId(id);
    }
}
