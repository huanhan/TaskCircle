package com.tc.service.impl;

import com.tc.db.entity.TaskStep;
import com.tc.db.entity.pk.TaskStepPK;
import com.tc.db.repository.TaskStepRepository;
import com.tc.exception.ValidException;
import com.tc.service.TaskStepService;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 任务步骤服务的实现
 *
 * @author Cyg
 */
@Service
public class TaskStepServiceImpl extends AbstractBasicServiceImpl<TaskStep> implements TaskStepService {
    @Autowired
    private TaskStepRepository taskStepRepository;


    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public List<TaskStep> findByTaskId(String taskId, Sort sort) {
        return taskStepRepository.findByTaskIdEquals(taskId, sort);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public TaskStep save(TaskStep taskStep) {
        List<TaskStep> queryTs = findByTaskId(taskStep.getTaskId(), new Sort(Sort.Direction.DESC, TaskStep.STEP));
        if (queryTs != null) {
            Integer max = 0;
            //对用户添加的内容进行数据库层的校验
            for (TaskStep ts :
                    queryTs) {
                if (ts.getTitle().equals(taskStep.getTitle())) {
                    throw new ValidException(StringResourceCenter.VALIDATOR_ADD_TITLE_FAILED);
                } else {
                    max = ts.getStep() > max ? ts.getStep() : max;
                }
            }
            //用户没有设置步骤时，使用数据库中最大的一个步骤+1
            if (taskStep.getStep() == null) {
                taskStep.setStep(max + 1);
            } else {
                //在用户设置步骤时，要对步骤进行校验
                if (taskStep.getStep() > max && (max + 1) != taskStep.getStep()) {
                    //用户设置的步骤，超过了数据库中最大的步骤+1
                    taskStep.setStep(max + 1);
                } else {
                    //用户设置的步骤小于最大步骤，择将数据库中的步骤后移
                    queryTs.forEach(ts -> {
                        if (ts.getStep() >= taskStep.getStep()) {
                            ts.setStep(ts.getStep() + 1);
                            updateStep(ts.getStep(), ts.getTaskId());
                        }
                    });
                }
            }
        } else {
            taskStep.setStep(1);
        }
        //保存数据
        return taskStepRepository.save(taskStep);
    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void saveAll(List<TaskStep> taskStep) {
        taskStepRepository.save(taskStep);
    }


    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void updateStep(Integer step, String id) {
        taskStepRepository.updateTaskStep(step, id);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public TaskStep findOne(TaskStepPK taskStepPK) {
        return taskStepRepository.findOne(taskStepPK);
    }

}
