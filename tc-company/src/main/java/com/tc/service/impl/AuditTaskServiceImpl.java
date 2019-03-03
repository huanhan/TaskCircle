package com.tc.service.impl;

import com.tc.db.entity.AuditTask;
import com.tc.db.repository.AuditTaskRepository;
import com.tc.service.AuditTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审核任务服务的实现
 * @author Cyg
 */
@Service
public class AuditTaskServiceImpl extends AbstractBasicServiceImpl<AuditTask> implements AuditTaskService {

    @Autowired
    private AuditTaskRepository auditTaskRepository;


    @Override
    public List<AuditTask> findByTaskId(String id) {
        return auditTaskRepository.findByTaskId(id);
    }
}
