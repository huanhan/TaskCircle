package com.tc.db.repository;

import com.tc.db.entity.UserIeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserIeRecordRepository extends JpaRepository<UserIeRecord,String> {
}
