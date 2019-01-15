package com.tc.service.impl;

import com.tc.controller.AuditController;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.repository.HunterTaskRepository;
import com.tc.db.repository.TaskRepository;
import com.tc.dto.task.QueryTask;
import com.tc.service.TaskService;
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
 * 任务服务的实现
 * @author Cyg
 */
@Service
public class TaskServiceImpl extends AbstractBasicServiceImpl<Task> implements TaskService {


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private HunterTaskRepository hunterTaskRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Task save(Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Page<Task> findByQueryTask(QueryTask queryTask) {
        return taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryTask.initPredicatesByTask(queryTask,root,query,cb);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryTask);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String id, TaskState state) {
        int count = taskRepository.updateState(id,state);
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Task findByIdAndHunters(String id) {
        Task task = findOne(id);
        List<HunterTask> hunterTasks = hunterTaskRepository.findBy(id,HunterTaskState.HUNTER_REPULSE);
        task.setHunterTasks(hunterTasks);
        return task;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(String id, TaskState state, Date date) {
        int count = taskRepository.updateState(id,state,new Timestamp(date.getTime()));
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean updateState(TaskState state) {

        //获取任务状态为审核中的状态，并且审核时长超过设置的审核时长
        List<Task> tasks = taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get(Task.TASK_STATE),TaskState.AUDIT));
            predicates.add(cb.lessThan(root.get(Task.ADMIN_AUDIT_TIME),new Timestamp(System.currentTimeMillis() - AuditController.AUDIT_LONG)));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (ListUtils.isEmpty(tasks)){
            return true;
        }else {
            List<String> ids = Task.toIds(tasks);
            int count = taskRepository.updateState(ids,state);
            return count > 0;
        }
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public Task findOne(String id) {
        return taskRepository.findOne(id);
    }
}
