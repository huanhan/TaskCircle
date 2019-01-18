package com.tc.service;

import com.tc.db.entity.Task;
import com.tc.db.enums.TaskState;
import com.tc.dto.task.QueryTask;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

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

    /**
     * 获取任务信息与任务执行者列表信息
     * @param id
     * @return
     */
    Task findByIdAndHunters(String id);

    /**
     * 修改任务状态与审核时间
     * @param id
     * @param state
     * @param date
     * @return
     */
    boolean updateState(String id, TaskState state, Date date);

    /**
     * 自动修改任务状态,根据时长判断，超过时长则修改任务为未审核状态
     * @return
     */
    boolean updateState(TaskState state);

    /**
     * 用户将任务提交审核
     * @param taskId 任务编号
     * @param state 任务状态
     * @return
     */
    boolean commitAudit(String taskId, TaskState state);

    /**
     * 用户放弃任务
     * @param id
     * @param taskId
     * @return
     */
    int abandonTask(Long id, Task taskId);

    /**
     * 保存发布的任务信息与扣除用户押金
     * @param task
     * @return
     */
    Task updateAndUserMoney(Task task);
}
