package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 任务审核
 */
@Entity
@Table(name = "audit_task", schema = "tc-company")
public class AuditTask implements Serializable {
    private Float money;
    private Audit audit;
    private Task task;


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
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        AuditTask auditTask = (AuditTask) o;
        return Float.compare(auditTask.money, money) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(audit.getId(), task.getId(), money);
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
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }
}
