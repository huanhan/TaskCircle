package com.tc.db.repository;

import com.tc.db.entity.TaskStep;
import com.tc.db.entity.pk.TaskStepPK;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 任务步骤仓库
 * @author Cyg
 */
public interface TaskStepRepository extends JpaRepository<TaskStep,TaskStepPK>,JpaSpecificationExecutor<TaskStep> {

    /**
     * 根据任务编号获取步骤
     * @param id
     * @return
     */
    List<TaskStep> findByTaskIdEquals(String id,Sort sort);

    /**
     * 修改任务步骤号
     * @param step
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update TaskStep ts set ts.step = :step where ts.taskId = :id")
    int updateTaskStep(@Param("step") Integer step,@Param("id") String id);


    /**
     * 获取任务下的步骤数量
     * @param taskId
     * @return
     */
    int countByTaskId(String taskId);

    /**
     * 删除某个任务所有步骤
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "delete from TaskStep t where t.taskId = :id ")
    int deleteByTaskId(@Param("id") String id);
}
