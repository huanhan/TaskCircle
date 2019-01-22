package com.tc.service;

import com.tc.db.entity.TaskStep;
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
}
