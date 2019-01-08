package com.tc.service;

import com.tc.db.entity.Task;
import com.tc.db.enums.TaskState;
import com.tc.dto.task.QueryTask;
import org.springframework.data.domain.Page;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskService extends BasicService<Task> {
    /**
     * 根据查询条件获取任务列表
     * @param queryTask
     * @return
     */
    Page<Task> findByQueryTask(QueryTask queryTask);

    /**
     * 修改任务状态
     * @param id
     * @param state
     * @return
     */
    boolean updateState(String id, TaskState state);
}
