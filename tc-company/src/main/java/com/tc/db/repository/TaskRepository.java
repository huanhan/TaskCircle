package com.tc.db.repository;

import com.tc.db.entity.Task;
import com.tc.db.enums.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 评论仓库
 * @author Cyg
 */
public interface TaskRepository extends JpaRepository<Task,String>,JpaSpecificationExecutor<Task> {

    @Modifying
    @Query(value = "update Task t set t.state = :state where t.id = :id")
    int updateState(@Param("id") String id,@Param("state") TaskState state);

}
