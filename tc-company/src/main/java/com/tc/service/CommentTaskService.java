package com.tc.service;

import com.tc.db.entity.CommentTask;
import com.tc.dto.comment.QueryTaskComment;
import org.springframework.data.domain.Page;

/**
 * 评论任务服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface CommentTaskService extends BasicService<CommentTask> {

    /**
     * 根据查询条件获取评论任务
     * @param queryTaskComment
     * @return
     */
    Page<CommentTask> findByQuery(QueryTaskComment queryTaskComment);

}
