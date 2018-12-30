package com.tc.db.repository;

import com.tc.db.entity.CommentTask;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论任务仓库
 * @author Cyg
 */
public interface CommentTaskRepository extends JpaRepository<CommentTask,Long> {
}
