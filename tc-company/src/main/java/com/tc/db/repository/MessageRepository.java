package com.tc.db.repository;

import com.tc.db.entity.Message;
import com.tc.db.enums.MessageState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 系统消息仓库
 * @author Cyg
 */
public interface MessageRepository extends JpaRepository<Message,Long>,JpaSpecificationExecutor<Message> {
    /**
     * 清除条件
     * @param id
     * @return
     */
    @Modifying
    @Query("update Message m set m.lookCondition = null,m.type = 'IS_NULL' where m.id = :id")
    int deleteCondition(@Param("id") Long id);

    /**
     * 更新状态
     * @param id
     * @param state
     * @return
     */
    @Modifying
    @Query("update Message m set m.state = :state where m.id = :id")
    int updateState(@Param("id") Long id, @Param("state") MessageState state);


    Message findById(Long id);
}
