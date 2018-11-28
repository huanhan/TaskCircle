package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 猎刃审核提示
 */
@Entity
@Table(name = "audit_hunter", schema = "tc-company")
public class AuditHunter implements Serializable {

    private Audit audit;
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        AuditHunter that = (AuditHunter) o;
        return user.getId().equals(that.user.getId()) &&
                Objects.equals(audit.getId(), that.getAudit().getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(audit.getId(), user.getId());
    }

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
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }
}
