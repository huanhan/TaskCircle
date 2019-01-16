package com.tc.service;

import com.tc.db.entity.CommentUser;
import com.tc.dto.comment.QueryUserComment;
import org.springframework.data.domain.Page;

/**
 * 评论用户服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface CommentUserService extends BasicService<CommentUser> {
    Long countByUserId(Long userid);

    /**
     * 根据查询条件获取用户评论
     * @param queryUserComment
     * @return
     */
    Page<CommentUser> findByQuery(QueryUserComment queryUserComment);

}
