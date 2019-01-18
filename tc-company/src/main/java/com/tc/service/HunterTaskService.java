package com.tc.service;

import com.tc.db.entity.HunterTask;
import com.tc.db.enums.HunterTaskState;
import com.tc.dto.task.QueryHunterTask;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * 猎刃任务服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface HunterTaskService extends BasicService<HunterTask> {
    /**
     * 查询猎刃任务
     * @param queryHunterTask
     * @return
     */
    Page<HunterTask> findByQueryHunterTask(QueryHunterTask queryHunterTask);

    /**
     * 自动更新任务状态
     * @param state
     */
    Boolean updateState(HunterTaskState state);

    /**
     * 根据状态和编号查询猎刃任务
     * @param id
     * @param type
     * @return
     */
    HunterTask findByIdAndState(String id, HunterTaskState type);

    /**
     * 锁定任务状态
     * @param id
     * @param state
     * @param date
     * @return
     */
    Boolean updateState(String id, HunterTaskState state, Date date);

    /**
     * 根据任务编号获取猎刃任务
     * @param taskId
     * @return
     */
    List<HunterTask> findByTaskId(String taskId);

    /**
     * 猎刃接任务
     * @param id
     * @param taskId
     * @return
     */
    boolean acceptTask(Long id, String taskId);

    /**
     * 猎刃开始任务
     * @param taskId
     * @return
     */
    boolean beginTask(String taskId);

    /**
     * 修改猎刃任务内容
     * @param id
     * @param context
     * @return
     */
    HunterTask update(String id, String context);
}
