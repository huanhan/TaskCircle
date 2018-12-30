package com.tc.db.repository;

import com.tc.db.entity.CommentUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface CommentUserRepository extends JpaRepository<CommentUser,Long> {
}
