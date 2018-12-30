package com.tc.db.repository;

import com.tc.db.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskRepository extends JpaRepository<Task,String> {
}
