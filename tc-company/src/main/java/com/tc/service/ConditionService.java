package com.tc.service;

import com.tc.db.entity.Condition;
import com.tc.dto.condition.QueryCondition;
import org.springframework.data.domain.Page;

/**
 * 消息条件服务接口
 * @author Cyg
 */
public interface ConditionService extends BasicService<Condition> {


    /**
     * 根据查询条件获取
     * @param queryCondition
     * @return
     */
    Page<Condition> findByQuery(QueryCondition queryCondition);

}
