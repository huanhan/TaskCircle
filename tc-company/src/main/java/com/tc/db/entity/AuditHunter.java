package com.tc.db.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * 猎刃审核记录
 * @author Cyg
 */
@Entity
@Table(name = "audit_hunter", schema = "tc-company")
public class AuditHunter {

    public static final String USER_ID = "userId";

    private String auditId;
    private Long userId;
    private Audit audit;
    private User user;

    public AuditHunter() {
    }

    public AuditHunter(String id, Long userId) {
        this.auditId = id;
        this.userId = userId;
    }

    public AuditHunter(Long userId) {
        this.userId = userId;
    }

    public AuditHunter(String id, Long userId, User user) {
        this.auditId = id;
        this.userId = userId;
        if (user != null){
            this.user = new User(user.getId(),user.getName(),user.getUsername());
        }
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
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditHunter that = (AuditHunter) o;
        return userId.equals(that.userId) &&
                Objects.equals(auditId, that.auditId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auditId, userId);
    }

    @OneToOne(mappedBy = "auditHunter")
    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static AuditHunter getBy(Audit audit) {
        AuditHunter result = new AuditHunter();
        if (audit != null){
            if (audit.getAuditHunter() != null){
                result.setUserId(audit.getAuditHunter().userId);
            }
            audit.setAuditHunter(null);
            result.setAuditId(audit.getId());
            result.setAudit(audit);
        }
        return result;
    }
}
