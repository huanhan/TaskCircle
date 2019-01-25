package com.tc.db.repository;

import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.entity.pk.TaskClassifyRelationPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 任务与分类关系仓库
 *
 * @author Cyg
 */
public interface TaskClassifyRelationRepository extends JpaRepository<TaskClassifyRelation, TaskClassifyRelationPK> {


    /**
     * 删除任务与分类关系
     *
     * @param ids 任务编号组
     * @param id  分类编号
     * @return
     */
    int deleteByTaskIdIsInAndTaskClassifyIdEquals(List<Long> ids, Long id);


    /**
     * 删除任务与分类关系
     *
     * @param ids 分类编号组
     * @param id  任务编号
     * @return
     */
    int deleteByTaskClassifyIdIsInAndTaskIdEquals(List<Long> ids, String id);


    /**
     * 删除某个任务的所有分类关系
     *
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "delete from TaskClassifyRelation t where t.taskId = :id ")
    int deleteByTaskId(@Param("id") String id);

    /**
     * 根据分类编号获取
     *
     * @param id
     * @return
     */
    List<TaskClassifyRelation> findByTaskClassifyIdEquals(Long id);

}
