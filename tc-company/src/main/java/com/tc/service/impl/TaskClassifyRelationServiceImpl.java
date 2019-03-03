package com.tc.service.impl;

import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.repository.TaskClassifyRelationRepository;
import com.tc.dto.LongIds;
import com.tc.dto.StringIds;
import com.tc.dto.task.classify.Remove;
import com.tc.service.TaskClassifyRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务分类关系服务的实现
 * @author Cyg
 */
@Service
public class TaskClassifyRelationServiceImpl extends AbstractBasicServiceImpl<TaskClassifyRelation> implements TaskClassifyRelationService {

    @Autowired
    private TaskClassifyRelationRepository taskClassifyRelationRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteByIds(StringIds ids) {
        int count = taskClassifyRelationRepository.deleteByTaskIdIsInAndTaskClassifyIdEquals(ids.getIds(),ids.getId());
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteBy(Remove remove) {
        int count = taskClassifyRelationRepository.deleteByTaskClassifyIdIsInAndTaskIdEquals(remove.getIds(),remove.getId());
        return count > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean addBy(Remove remove) {
        List<TaskClassifyRelation> ops = new ArrayList<>();
        remove.getIds().forEach(aLong -> {
            ops.add(new TaskClassifyRelation(aLong,remove.getId()));
        });
        List<TaskClassifyRelation> result = taskClassifyRelationRepository.save(ops);
        return result.size() > 0;
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<TaskClassifyRelation> findByTask(String taskID) {
        return taskClassifyRelationRepository.findByTaskId(taskID);
    }
}
