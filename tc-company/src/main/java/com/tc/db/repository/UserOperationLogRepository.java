package com.tc.db.repository;

import com.tc.db.entity.UserOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserOperationLogRepository extends JpaRepository<UserOperationLog,String> {
}
