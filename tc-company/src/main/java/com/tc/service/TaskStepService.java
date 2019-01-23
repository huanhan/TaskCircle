package com.tc.service;

import com.tc.db.entity.TaskStep;
import com.tc.db.entity.pk.TaskStepPK;
import com.tc.db.enums.TaskState;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务步骤服务
 * @author Cyg
 */
public interface TaskStepService extends BasicService<TaskStep> {

    /**
     * 获取指定任务的所有步骤
     * @param taskId
     * @param sort
     * @return
     */
    List<TaskStep> findByTaskId(String taskId,Sort sort);

    void saveAll(List<TaskStep> taskStep);

    /**
     * 更新任务步骤标识号
     * @param step
     * @param id
     */
    void updateStep(Integer step, String id);

    /**
     * 根据联合主键查询
     * @param taskStepPK
     * @return
     */
    TaskStep findOne(TaskStepPK taskStepPK);
}
