package com.tc.db.entity;

import com.tc.until.ListUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 审核用户提现
 * @author Cyg
 */
@Entity
@Table(name = "audit_withdraw", schema = "tc-company")
public class AuditWithdraw {

    public static final String USER_WITHDRAW = "userWithdraw";
    public static final String WITHDRAW_ID = "withdrawId";
    public static final String AUDIT_ID = "auditId";

    private String auditId;
    private String withdrawId;
    private Audit audit;
    private UserWithdraw userWithdraw;
    private Float adminMoney;
    private Float userMoney;


    public AuditWithdraw() {
    }

    public AuditWithdraw(String id, String withdrawId) {
        this.auditId = id;
        this.withdrawId = withdrawId;
    }

    public AuditWithdraw(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    public AuditWithdraw(String auditId, String withdrawId, Float adminMoney, Float userMoney) {
        this.auditId = auditId;
        this.withdrawId = withdrawId;
        this.adminMoney = adminMoney;
        this.userMoney = userMoney;
    }

    public AuditWithdraw(String id, String withdrawId, UserWithdraw userWithdraw) {
        this.auditId = id;
        this.withdrawId = withdrawId;
        if (userWithdraw != null){
            this.userWithdraw = new UserWithdraw(userWithdraw.getUserId());
        }
    }

    public static List<AuditWithdraw> toIndexList(List<AuditWithdraw> awList) {
        if (ListUtils.isNotEmpty(awList)){
            awList.forEach(auditWithdraw -> {
                if (auditWithdraw.audit != null){
                    auditWithdraw.setAudit(Audit.toDetail(auditWithdraw.audit));
                }
                auditWithdraw.setUserWithdraw(null);
            });
        }
        return awList;
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

    @Basic
    @Column(name = "admin_money")
    public Float getAdminMoney() {
        return adminMoney;
    }

    public void setAdminMoney(Float adminMoney) {
        this.adminMoney = adminMoney;
    }

    @Basic
    @Column(name = "user_money")
    public Float getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(Float userMoney) {
        this.userMoney = userMoney;
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
                if (audit.getAuditWithdraw().userWithdraw != null){
                    result.setUserWithdraw(UserWithdraw.toDetail(audit.getAuditWithdraw().userWithdraw));
                }
            }
            audit.setAuditWithdraw(null);
            result.setAuditId(audit.getId());
            result.setAudit(Audit.toDetail(audit));
        }
        return result;
    }
}
