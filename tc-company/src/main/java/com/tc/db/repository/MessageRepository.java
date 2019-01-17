package com.tc.db.repository;

import com.tc.db.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 系统消息仓库
 * @author Cyg
 */
public interface MessageRepository extends JpaRepository<Message,Long>,JpaSpecificationExecutor<Message> {
}
