package com.tc.service;

import com.tc.db.entity.HunterTask;
import com.tc.db.enums.HunterTaskState;
import com.tc.dto.task.QueryHunterTask;
import org.springframework.data.domain.Page;

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
}
