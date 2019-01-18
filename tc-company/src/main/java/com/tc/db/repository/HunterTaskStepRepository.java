package com.tc.db.repository;

import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.pk.HunterTaskStepPK;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 评论仓库
 *
 * @author Cyg
 */
public interface HunterTaskStepRepository extends JpaRepository<HunterTaskStep, HunterTaskStepPK> {

    List<HunterTaskStep> findByHunterTaskId(String id, Sort sort);

    /**
     * 获取猎刃任务对应的步骤的数量
     * @param id
     * @return
     */
    int countByHunterTaskId(String id);
}
