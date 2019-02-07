package com.tc.service;

import com.tc.db.entity.Comment;
import com.tc.dto.comment.QueryComment;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评论服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface CommentService extends BasicService<Comment> {

    Page<Comment> findByQuery(QueryComment queryComment);

}
