package com.tc.db.repository;

import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.entity.pk.TaskClassifyRelationPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskClassifyRelationRepository extends JpaRepository<TaskClassifyRelation,TaskClassifyRelationPK> {
}
