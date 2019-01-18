package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.repository.HunterTaskRepository;
import com.tc.dto.task.QueryHunterTask;
import com.tc.service.HunterTaskService;
import com.tc.until.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 猎刃任务服务的实现
 * @author Cyg
 */
@Service
public class HunterTaskServiceImpl extends AbstractBasicServiceImpl<HunterTask> implements HunterTaskService {

    @Autowired
    private HunterTaskRepository hunterTaskRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<HunterTask> findByQueryHunterTask(QueryHunterTask queryHunterTask) {
        return hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryHunterTask.initPredicatesByHunterTask(queryHunterTask,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryHunterTask);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(HunterTaskState state) {
        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<HunterTask> tasks = hunterTaskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(HunterTask.HUNTER_TASK_STATE),HunterTaskState.ADMIN_ADUIT));
            predicates.add(cb.lessThan(root.get(HunterTask.ADMIN_AUDIT_TIME),new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(tasks)){
            return true;
        }else {
            List<String> ids = HunterTask.toIds(tasks);
            int count = hunterTaskRepository.updateState(ids,state);
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public HunterTask findByIdAndState(String id, HunterTaskState type) {
        return hunterTaskRepository.findByIdAndState(id,type);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean updateState(String id, HunterTaskState state, Date date) {
        int count = hunterTaskRepository.updateState(id,state,new Timestamp(date.getTime()));
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<HunterTask> findByTaskId(String taskId) {
        return hunterTaskRepository.findByTaskId(taskId);
    }
}
