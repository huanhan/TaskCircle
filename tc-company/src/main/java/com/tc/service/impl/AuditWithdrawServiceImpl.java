package com.tc.service.impl;

import com.tc.db.entity.AuditWithdraw;
import com.tc.db.repository.AuditWithdrawRepository;
import com.tc.service.AuditWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审核提现服务的实现
 * @author Cyg
 */
@Service
public class AuditWithdrawServiceImpl extends AbstractBasicServiceImpl<AuditWithdraw> implements AuditWithdrawService {

    @Autowired
    private AuditWithdrawRepository auditWithdrawRepository;

    @Override
    public List<AuditWithdraw> findByWithdrawId(String id) {
        return auditWithdrawRepository.findByWithdrawId(id);
    }
}
