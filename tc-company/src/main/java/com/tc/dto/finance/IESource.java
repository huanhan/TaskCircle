package com.tc.dto.finance;

import com.tc.db.entity.AuditWithdraw;
import com.tc.db.entity.User;
import com.tc.db.enums.WithdrawState;
import com.tc.db.enums.WithdrawType;

import java.sql.Timestamp;
import java.util.Collection;

/**
 * 收支来源
 * @author Cyg
 */
public class IESource {

    private String id;
    private Long userId;
    private Float money;
    private Float realityMoney;
    private String context;
    private WithdrawState state;
    private WithdrawType type;
    private Timestamp createTime;
    private Timestamp adminAuditTime;
    private Timestamp auditPassTime;
    private Collection<AuditWithdraw> auditWithdraws;
    private User user;

}
