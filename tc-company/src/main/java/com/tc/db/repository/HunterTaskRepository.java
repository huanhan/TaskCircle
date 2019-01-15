package com.tc.db.repository;

import com.tc.db.entity.HunterTask;
import com.tc.db.enums.HunterTaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface HunterTaskRepository extends JpaRepository<HunterTask,String>,JpaSpecificationExecutor<HunterTask> {

    /**
     * 根据任务编号与状态查询
     * @param taskId
     * @param hunterTaskState
     * @return
     */
    @Query(value = "select t from HunterTask t where t.taskId = :id and t.state = :state")
    List<HunterTask> findBy(@Param("id") String taskId,@Param("state") HunterTaskState hunterTaskState);

    /**
     * 更新任务状态，并将审核时间制空
     * @param ids
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.adminAuditTime = NULL where t.id in (:ids)")
    int updateState(@Param("ids") List<String> ids,@Param("state") HunterTaskState state);
}
