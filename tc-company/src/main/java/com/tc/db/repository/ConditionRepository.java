package com.tc.db.repository;

import com.tc.db.entity.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 消息查询条件仓库
 * @author Cyg
 */
public interface ConditionRepository extends JpaRepository<Condition,Long>,JpaSpecificationExecutor<Condition> {
}
