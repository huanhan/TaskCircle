package com.tc.db.repository;

import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.entity.pk.TaskClassifyRelationPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 任务与分类关系仓库
 * @author Cyg
 */
public interface TaskClassifyRelationRepository extends JpaRepository<TaskClassifyRelation,TaskClassifyRelationPK> {


    /**
     * 删除任务与分类关系
     * @param ids 任务编号组
     * @param id 分类编号
     * @return
     */
    int deleteByTaskIdIsInAndTaskClassifyIdEquals(List<Long> ids,Long id);

    /**
     * 根据分类编号获取
     * @param id
     * @return
     */
    List<TaskClassifyRelation> findByTaskClassifyIdEquals(Long id);

}
