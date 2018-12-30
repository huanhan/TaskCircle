package com.tc.db.repository;

import com.tc.db.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface MessageRepository extends JpaRepository<Message,Long> {
}
