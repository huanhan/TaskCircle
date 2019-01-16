package com.tc.service;

import com.tc.db.entity.CommentHunter;
import com.tc.dto.comment.QueryHunterComment;
import org.springframework.data.domain.Page;

/**
 * 评论猎刃服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface CommentHunterService extends BasicService<CommentHunter> {

    /**
     * 根据查询条件获取猎刃评论
     * @param queryHunterComment
     * @return
     */
    Page<CommentHunter> findByQuery(QueryHunterComment queryHunterComment);
}
