package com.tc.db.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * 审核需发布的任务
 * @author Cyg
 */
@Entity
@Table(name = "audit_task", schema = "tc-company")
public class AuditTask {

    public static final String TASK = "task";

    private String auditId;
    private String taskId;
    private Float money;
    private Audit audit;
    private Task task;

    public AuditTask() {
    }

    public AuditTask(String auditId, String taskId, Float money) {
        this.auditId = auditId;
        this.taskId = taskId;
        this.money = money;
    }

    public AuditTask(String taskId, Float money) {
        this.taskId = taskId;
        this.money = money;
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
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "money")
    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditTask auditTask = (AuditTask) o;
        return Double.compare(auditTask.money, money) == 0 &&
                Objects.equals(auditId, auditTask.auditId) &&
                Objects.equals(taskId, auditTask.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(auditId, taskId, money);
    }

    @OneToOne(mappedBy = "auditTask")
    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public static AuditTask getBy(Audit audit) {
        AuditTask result = new AuditTask();
        if (audit != null){
            if (audit.getAuditTask() != null){
                result.setTaskId(audit.getAuditTask().taskId);
            }
            audit.setAuditTask(null);
            result.setAuditId(audit.getId());
            result.setAudit(audit);
        }
        return result;
    }
}
