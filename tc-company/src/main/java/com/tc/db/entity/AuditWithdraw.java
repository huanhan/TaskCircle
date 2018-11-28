package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 提现审核
 */
@Entity
@Table(name = "audit_withdraw", schema = "tc-company")
public class AuditWithdraw implements Serializable {

    private Audit audit;
    private UserWithdraw userWithdraw;

    @Id
    @OneToOne
    @JoinColumn(name = "audit_id", referencedColumnName = "id", nullable = false)
    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit auditByAuditId) {
        this.audit = auditByAuditId;
    }

    @ManyToOne
    @JoinColumn(name = "withdraw_id", referencedColumnName = "id", nullable = false)
    public UserWithdraw getUserWithdraw() {
        return userWithdraw;
    }

    public void setUserWithdraw(UserWithdraw userWithdrawByWithdrawId) {
        this.userWithdraw = userWithdrawByWithdrawId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        AuditWithdraw that = (AuditWithdraw) o;
        return Objects.equals(audit.getId(), that.getAudit().getId()) &&
                Objects.equals(userWithdraw.getId(), that.getUserWithdraw().getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(audit.getId(), userWithdraw.getId());
    }
}
