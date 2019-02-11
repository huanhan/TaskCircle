package com.tc.service;

import com.tc.db.entity.Task;
import com.tc.db.enums.TaskState;
import com.tc.dto.TimeScope;
import com.tc.dto.task.QueryTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 评论仓库
 *
 * @author Cyg
 */
public interface TaskService extends BasicService<Task> {
    /**
     * 修改任务
     *
     * @param task
     * @return
     */
    Task modify(Task task);

    /**
     * 根据查询条件获取任务列表
     *
     * @param queryTask
     * @return
     */
    Page<Task> findByQueryTask(QueryTask queryTask);

    /**
     * 根据条件获取任务列表
     *
     * @param queryTask
     * @return
     */
    List<Task> findByQueryTaskAndNotPage(QueryTask queryTask);

    /**
     * 修改任务状态
     *
     * @param id
     * @param state
     * @return
     */
    boolean updateState(String id, TaskState state);

    /**
     * 获取任务信息与任务执行者列表信息
     *
     * @param id
     * @return
     */
    Task findByIdAndHunters(String id);

    /**
     * 修改任务状态与审核时间
     *
     * @param id
     * @param state
     * @param date
     * @return
     */
    boolean updateState(String id, TaskState state, Date date);

    /**
     * 自动修改任务状态,根据时长判断，超过时长则修改任务为未审核状态
     *
     * @param state
     * @return
     */
    boolean updateState(TaskState state);

    /**
     * 用户将任务提交审核
     *
     * @param taskId 任务编号
     * @param state  任务状态
     * @return
     */
    boolean commitAudit(String taskId, TaskState state);

    /**
     * 用户取消审核
     *
     * @param taskId
     * @param state
     * @return
     */
    boolean diCommitAudit(String taskId, TaskState state);

    /**
     * 用户放弃任务
     *
     * @param id
     * @param taskId
     * @return
     */
    int abandonTask(Long id, Task taskId);

    /**
     * 保存发布的任务信息与扣除用户押金
     *
     * @param task
     * @return
     */
    Task updateAndUserMoney(Task task);

    /**
     * 用户撤回任务
     *
     * @param task
     * @return
     */
    boolean outTask(Task task);

    /**
     * 判断任务是否成功
     *
     * @param taskId
     * @return
     */
    boolean taskIsSuccess(String taskId);

    /**
     * 判断任务是否完全被拒绝
     *
     * @param id
     * @return
     */
    boolean taskIsReject(String id);

    /**
     * 判断任务是否可放弃，可放弃则直接放弃
     *
     * @param task
     * @return
     */
    boolean hasAbandon(Task task);

    /**
     * 用户取消放弃任务
     *
     * @param id
     * @param task
     * @return
     */
    boolean diAbandonTask(Long id, Task task);


    /**
     * 管理员修改任务状态
     *
     * @param id
     * @param state
     * @return
     */
    boolean adminUpdateState(String id, TaskState state);

    /**
     * 获取用户的押金列表
     *
     * @param id
     * @param scope
     * @return
     */
    Page<Task> findCashPledge(Long id, TimeScope scope);

    Page<Task> search(String key, Pageable pageable);

    List<Task> taskByDistance(Double lat, Double log );
}
