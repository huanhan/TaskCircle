package com.tc.db.repository;

import com.tc.db.entity.Hunter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface HunterRepository extends JpaRepository<Hunter,Long> {
}
