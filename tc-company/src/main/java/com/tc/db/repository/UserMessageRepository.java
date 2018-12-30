package com.tc.db.repository;

import com.tc.db.entity.UserMessage;
import com.tc.db.entity.pk.UserMessagePK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserMessageRepository extends JpaRepository<UserMessage,UserMessagePK> {
}
