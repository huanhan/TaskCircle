package com.tc.service.impl;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.TaskStep;
import com.tc.db.entity.pk.HunterTaskStepPK;
import com.tc.db.entity.pk.TaskStepPK;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.repository.HunterTaskRepository;
import com.tc.db.repository.HunterTaskStepRepository;
import com.tc.db.repository.TaskStepRepository;
import com.tc.exception.DBException;
import com.tc.service.HunterTaskStepService;
import com.tc.until.StringResourceCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 猎刃任务步骤服务的实现
 *
 * @author Cyg
 */
@Service
public class HunterTaskStepServiceImpl extends AbstractBasicServiceImpl<HunterTaskStep> implements HunterTaskStepService {
    @Autowired
    private HunterTaskStepRepository hunterTaskStepRepository;

    @Autowired
    private HunterTaskRepository hunterTaskRepository;

    @Autowired
    private TaskStepRepository taskStepRepository;

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public List<HunterTaskStep> findByHunterTaskId(String id, Sort sort) {
        return hunterTaskStepRepository.findByHunterTaskId(id,sort);
    }

    @Transactional(rollbackFor = RuntimeException.class,readOnly = true)
    @Override
    public HunterTaskStep findOne(HunterTaskStepPK hunterTaskStepPK) {
        return hunterTaskStepRepository.findOne(hunterTaskStepPK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public HunterTaskStep save(HunterTaskStep hunterTaskStep) {

        //获取步骤对应的猎刃任务
        HunterTask hunterTask = hunterTaskStep.getHunterTask();
        if (hunterTask == null){
            hunterTask = hunterTaskRepository.findOne(hunterTaskStep.getHunterTaskId());
            if (hunterTask == null){
                throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
            }
        }

        //验证新增时对应的步骤
        TaskStep taskStep = taskStepRepository.findOne(new TaskStepPK(hunterTask.getTaskId(),hunterTaskStep.getStep()));
        if (taskStep == null){
            throw new DBException("步骤不正确，在任务中没有匹配的步骤");
        }

        //判断是否第一次添加步骤
        int count = hunterTaskStepRepository.countByHunterTaskId(hunterTask.getId());
        if (count == 0 && hunterTask.getState().equals(HunterTaskState.BEGIN)){
            count = hunterTaskRepository.updateState(hunterTask.getId(),HunterTaskState.EXECUTORY);
            if (count <= 0){
                throw new DBException("修改猎刃任务状态失败");
            }
        }

        //保存步骤
        return hunterTaskStepRepository.save(hunterTaskStep);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public HunterTaskStep update(HunterTaskStep user) {
        return super.update(user);
    }
}
