package com.tc.service;

import com.tc.db.entity.TaskClassifyRelation;
import com.tc.dto.task.classify.Remove;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskClassifyRelationService extends BasicService<TaskClassifyRelation> {

    /**
     * 移除任务中的指定分类
     * @param remove
     * @return
     */
    boolean deleteBy(Remove remove);

    /**
     * 给任务添加分类
     * @param remove
     * @return
     */
    boolean addBy(Remove remove);

    /**
     * 根据任务编号查询
     * @param taskID
     * @return
     */
    List<TaskClassifyRelation> findByTask(String taskID);
}
