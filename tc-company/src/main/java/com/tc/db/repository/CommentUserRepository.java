package com.tc.db.repository;

import com.tc.db.entity.CommentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 评论仓库
 *
 * @author Cyg
 */
public interface CommentUserRepository extends JpaRepository<CommentUser, Long>,JpaSpecificationExecutor<CommentUser> {
    Long countByUserId(Long userId);
}
