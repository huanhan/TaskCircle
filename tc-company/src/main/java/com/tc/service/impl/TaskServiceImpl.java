package com.tc.service.impl;

import com.tc.db.entity.Task;
import com.tc.db.enums.TaskState;
import com.tc.db.repository.TaskRepository;
import com.tc.dto.task.QueryTask;
import com.tc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 任务服务的实现
 * @author Cyg
 */
@Service
public class TaskServiceImpl extends AbstractBasicServiceImpl<Task> implements TaskService {


    @Autowired
    private TaskRepository taskRepository;

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
    public Task findOne(String id) {
        return taskRepository.findOne(id);
    }
}
