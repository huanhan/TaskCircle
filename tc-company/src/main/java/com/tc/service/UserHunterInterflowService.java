package com.tc.service;

import com.tc.db.entity.UserHunterInterflow;
import com.tc.dto.task.QueryTaskInterflow;
import org.springframework.data.domain.Page;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserHunterInterflowService extends BasicService<UserHunterInterflow> {
    /**
     * 根据查询条件获取
     * @param queryTaskInterflow
     * @return
     */
    Page<UserHunterInterflow> findByQuery(QueryTaskInterflow queryTaskInterflow);

    /**
     * 根据条件进行分组查询
     * @param queryTaskInterflow
     * @return
     */
    Page<UserHunterInterflow> findByQueryAndGroup(QueryTaskInterflow queryTaskInterflow);
}
