package com.tc.db.repository;

import com.tc.db.entity.TaskStep;
import com.tc.db.entity.pk.TaskStepPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskStepRepository extends JpaRepository<TaskStep,TaskStepPK> {
}
