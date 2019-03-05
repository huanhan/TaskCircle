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
import com.tc.exception.ValidException;
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

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public List<HunterTaskStep> findByHunterTaskId(String id, Sort sort) {
        return hunterTaskStepRepository.findByHunterTaskId(id, sort);
    }

    @Transactional(rollbackFor = RuntimeException.class, readOnly = true)
    @Override
    public HunterTaskStep findOne(HunterTaskStepPK hunterTaskStepPK) {
        return hunterTaskStepRepository.findOne(hunterTaskStepPK);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public boolean deleteById(HunterTaskStepPK hunterTaskStepPK) {
        int count;

        HunterTaskStep hunterTaskStep = hunterTaskStepRepository.findOne(hunterTaskStepPK);
        if (hunterTaskStep == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        HunterTask hunterTask = hunterTaskStep.getHunterTask();


        //验证指定的猎刃任务是否可以删除
        if (!hunterTask.getState().equals(HunterTaskState.EXECUTE)
                || !hunterTask.getState().equals(HunterTaskState.TASK_COMPLETE)) {
            throw new ValidException(StringResourceCenter.VALIDATOR_TASK_STATE_FAILED);
        }


        //删除的时候需要判断猎刃任务状态是不是已经完成，如果已经完成，则需要调整成未完成
        if (hunterTaskStep.getHunterTask().getState().equals(HunterTaskState.TASK_COMPLETE)) {
            count = hunterTaskRepository.updateState(hunterTaskStep.getHunterTaskId(), HunterTaskState.EXECUTE);
            if (count <= 0) {
                throw new DBException("修改猎刃任务状态失败");
            }
        }
        try {
            hunterTaskStepRepository.delete(hunterTaskStepPK);
        } catch (Exception e) {
            throw new DBException(StringResourceCenter.DB_DELETE_FAILED);
        }

        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public HunterTaskStep save(HunterTaskStep hunterTaskStep) {

        //获取步骤对应的猎刃任务
        HunterTask hunterTask = hunterTaskStep.getHunterTask();
        if (hunterTask == null) {
            hunterTask = hunterTaskRepository.findOne(hunterTaskStep.getHunterTaskId());
            if (hunterTask == null) {
                throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
            }
        }

        //验证新增时对应的步骤
        TaskStep taskStep = taskStepRepository.findOne(new TaskStepPK(hunterTask.getTaskId(), hunterTaskStep.getStep()));
        if (taskStep == null) {
            throw new DBException("步骤不正确，在任务中没有匹配的步骤");
        }

        //已完成步骤数
        int count = hunterTaskStepRepository.countByHunterTaskId(hunterTask.getId());
        //总步骤数
        int tsCount = taskStepRepository.countByTaskId(hunterTask.getTaskId());
        if (count == 0 && hunterTask.getState().equals(HunterTaskState.BEGIN) && 1 != tsCount) {
            count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.EXECUTE);
            if (count <= 0) {
                throw new DBException("修改猎刃任务状态失败");
            }
        } else {
            //判断是否最后一次添加,如果最后一次添加，修改猎刃任务的状态未已完成
//            tsCount = taskStepRepository.countByTaskId(hunterTask.getHunterTaskId());
            if (count + 1 == tsCount) {
                count = hunterTaskRepository.updateState(hunterTask.getId(), HunterTaskState.TASK_COMPLETE);
                if (count <= 0) {
                    throw new DBException("修改猎刃任务状态失败");
                }
            }
        }

        //保存步骤
        return hunterTaskStepRepository.save(hunterTaskStep);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public HunterTaskStep update(HunterTaskStep hunterTaskStep) {
        return hunterTaskStepRepository.save(hunterTaskStep);
    }
}
