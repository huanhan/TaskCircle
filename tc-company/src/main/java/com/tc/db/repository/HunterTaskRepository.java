package com.tc.db.repository;

import com.tc.db.entity.Hunter;
import com.tc.db.entity.HunterTask;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
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
     * 根据猎刃任务编号与状态查询
     * @param taskId
     * @param hunterTaskState
     * @return
     */
    @Query(value = "select t from HunterTask t where t.id = :id and t.state = :state")
    HunterTask findByIdAndState(@Param("id") String taskId,@Param("state") HunterTaskState hunterTaskState);

    /**
     * 更新任务状态，并将审核时间制空
     * @param ids
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.adminAuditTime = NULL where t.id in (:ids)")
    int updateState(@Param("ids") List<String> ids,@Param("state") HunterTaskState state);

    /**
     * 更新任务状态，并将审核时间制空
     * @param state
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state where t.id = :id")
    int updateState(@Param("id") String id,@Param("state") HunterTaskState state);

    /**
     * 更新猎刃任务状态与审核时间
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update HunterTask t set t.state = :state, t.adminAuditTime = :time where t.id = :id")
    int updateState(@Param("id") String id, @Param("state") HunterTaskState state, @Param("time")Timestamp timestamp);
}
