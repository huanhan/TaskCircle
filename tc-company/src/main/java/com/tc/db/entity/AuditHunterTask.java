package com.tc.db.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * 审核猎刃任务
 * @author Cyg
 */
@Entity
@Table(name = "audit_hunter_task", schema = "tc-company")
public class AuditHunterTask {
    public static final String AUDIT_ID = "auditId";
    public static final String HUNTER_TASK_ID = "hunterTaskId";
    private String auditId;
    private String hunterTaskId;
    private Audit audit;
    private HunterTask hunterTask;

    public AuditHunterTask() {
    }

    public AuditHunterTask(String id, String taskId) {
        this.auditId = id;
        this.hunterTaskId = taskId;
    }

    public AuditHunterTask(String taskId) {
        this.hunterTaskId = taskId;
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
    @Column(name = "hunter_task_id")
    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditHunterTask that = (AuditHunterTask) o;
        return Objects.equals(auditId, that.auditId) &&
                Objects.equals(hunterTaskId, that.hunterTaskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(auditId, hunterTaskId);
    }

    @OneToOne(mappedBy = "auditHunterTask")
    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public HunterTask getHunterTask() {
        return hunterTask;
    }

    public void setHunterTask(HunterTask hunterTask) {
        this.hunterTask = hunterTask;
    }
}
