package com.tc.db.repository;

import com.tc.db.entity.HunterTask;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface HunterTaskRepository extends JpaRepository<HunterTask,String> {
}
