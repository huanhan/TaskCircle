package com.tc.db.repository;

import com.tc.db.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
