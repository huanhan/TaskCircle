package com.tc.service;

import com.tc.db.entity.Hunter;
import com.tc.dto.user.hunter.QueryHunter;
import org.springframework.data.domain.Page;

/**
 * 猎刃服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface HunterService extends BasicService<Hunter> {
    /**
     * 根据查询条件获取猎刃列表
     * @param queryHunter
     * @return
     */
    Page<Hunter> findByQuery(QueryHunter queryHunter);
}
