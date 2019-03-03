package com.tc.service;

import com.tc.db.entity.Audit;
import com.tc.dto.audit.QueryAudit;
import com.tc.dto.trans.Trans;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 审核服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AuditService extends BasicService<Audit> {

    /**
     * 根据查询条件获取审核记录
     * @param queryAudit
     * @return
     */
    Page<Audit> findByQueryAudit(QueryAudit queryAudit);

    /**
     * 根据查询条件获取指定用户的审核记录
     * @param queryAudit
     * @return
     */
    Page<Audit> findByQueryAndUser(QueryAudit queryAudit);

    /**
     * 获取用户需要审核的数据统计信息
     * @return
     */
    List<Trans> findByAudit();

}
