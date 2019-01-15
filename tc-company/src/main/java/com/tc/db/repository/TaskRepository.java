package com.tc.db.repository;

import com.tc.db.entity.Task;
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
public interface TaskRepository extends JpaRepository<Task,String>,JpaSpecificationExecutor<Task> {

    /**
     * 更新任务状态
     * @param id
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update Task t set t.state = :state where t.id = :id")
    int updateState(@Param("id") String id,@Param("state") TaskState state);

    /**
     * 更新任务状态与管理员审核时间
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update Task t set t.state = :state, t.adminAuditTime = :time where t.id = :id")
    int updateStateAndAdminAuditTime(@Param("id") String id, @Param("state") TaskState state, @Param("time")Timestamp timestamp);

    /**
     * 更新任务状态与任务发布时间
     * @param id
     * @param state
     * @param timestamp
     * @return
     */
    @Modifying
    @Query(value = "update Task t set t.state = :state, t.issueTime = :time where t.id = :id")
    int updateStateAndIssueTime(@Param("id") String id, @Param("state") TaskState state, @Param("time")Timestamp timestamp);


    /**
     * 更新任务状态，并将审核时间制空
     * @param ids
     * @param state
     * @return
     */
    @Modifying
    @Query(value = "update Task t set t.state = :state, t.adminAuditTime = NULL where t.id in (:ids)")
    int updateState(@Param("ids") List<String> ids, @Param("state") TaskState state);

}
