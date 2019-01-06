package com.tc.service.impl;

import com.tc.db.entity.Audit;
import com.tc.db.entity.AuditHunter;
import com.tc.db.repository.AuditRepository;
import com.tc.dto.audit.QueryAudit;
import com.tc.service.AuditHunterService;
import com.tc.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * 审核服务的实现
 * @author Cyg
 */
@Service
public class AuditServiceImpl extends AbstractBasicServiceImpl<Audit> implements AuditService {

    @Autowired
    private AuditRepository auditRepository;

    @Override
    public Page<Audit> findByQueryAudit(QueryAudit queryAudit) {
        return auditRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = QueryAudit.initPredicates(queryAudit,root,query,cb);
            if (queryAudit.getAdminId() != null && queryAudit.getAdminId() > 0){
                predicates.add(cb.equal(root.get(Audit.ADMIN_ID),queryAudit.getAdminId()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        },queryAudit);
    }
}
