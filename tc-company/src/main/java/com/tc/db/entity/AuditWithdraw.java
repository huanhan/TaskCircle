package com.tc.db.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * 审核用户提现
 * @author Cyg
 */
@Entity
@Table(name = "audit_withdraw", schema = "tc-company")
public class AuditWithdraw {

    public static final String USER_WITHDRAW = "userWithdraw";

    private String auditId;
    private String withdrawId;
    private Audit audit;
    private UserWithdraw userWithdraw;

    public AuditWithdraw() {
    }

    public AuditWithdraw(String id, String withdrawId) {
        this.auditId = id;
        this.withdrawId = withdrawId;
    }

    public AuditWithdraw(String withdrawId) {
        this.withdrawId = withdrawId;
    }


    @Id
    @Column(name = "audit_id")
    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    @Basic
    @Column(name = "withdraw_id")
    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditWithdraw that = (AuditWithdraw) o;
        return Objects.equals(auditId, that.auditId) &&
                Objects.equals(withdrawId, that.withdrawId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(auditId, withdrawId);
    }

    @OneToOne(mappedBy = "auditWithdraw")
    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    @ManyToOne
    @JoinColumn(name = "withdraw_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public UserWithdraw getUserWithdraw() {
        return userWithdraw;
    }

    public void setUserWithdraw(UserWithdraw userWithdraw) {
        this.userWithdraw = userWithdraw;
    }

    public static AuditWithdraw getBy(Audit audit) {
        AuditWithdraw result = new AuditWithdraw();
        if (audit != null){
            if (audit.getAuditWithdraw() != null){
                result.setWithdrawId(audit.getAuditWithdraw().withdrawId);
            }
            audit.setAuditWithdraw(null);
            result.setAuditId(audit.getId());
            result.setAudit(audit);
        }
        return result;
    }
}
