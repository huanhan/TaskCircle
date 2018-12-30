package com.tc.db.repository;

import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.pk.HunterTaskStepPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface HunterTaskStepRepository extends JpaRepository<HunterTaskStep,HunterTaskStepPK> {
}
