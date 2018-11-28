package com.tc.db.entity;

import com.tc.db.enums.AuditType;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * @author Cyg
 * 审核记录表
 */
@Entity
public class Audit implements Serializable {

    private String id;
    private String idea;
    private String result;
    private String reason;
    private AuditType type;
    private Timestamp createTime;
    private Admin creation;
    private AuditHunter auditHunter;
    private AuditTask auditTask;
    private AuditWithdraw auditWithdraw;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "idea")
    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    @Basic
    @Column(name = "result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Basic
    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public AuditType getType() {
        return type;
    }

    public void setType(AuditType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id", nullable = false)
    public Admin getCreation() {
        return creation;
    }

    public void setCreation(Admin adminByAdminId) {
        this.creation = adminByAdminId;
    }

    @OneToOne(mappedBy = "audit")
    public AuditHunter getAuditHunter() {
        return auditHunter;
    }

    public void setAuditHunter(AuditHunter auditHunterById) {
        this.auditHunter = auditHunterById;
    }

    @OneToOne(mappedBy = "audit")
    public AuditTask getAuditTask() {
        return auditTask;
    }

    public void setAuditTask(AuditTask auditTaskById) {
        this.auditTask = auditTaskById;
    }

    @OneToOne(mappedBy = "audit")
    public AuditWithdraw getAuditWithdraw() {
        return auditWithdraw;
    }

    public void setAuditWithdraw(AuditWithdraw auditWithdrawById) {
        this.auditWithdraw = auditWithdrawById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Audit audit = (Audit) o;
        return creation.getUser().getId().equals(audit.creation.getUser().getId()) &&
                Objects.equals(id, audit.id) &&
                Objects.equals(idea, audit.idea) &&
                Objects.equals(result, audit.result) &&
                Objects.equals(reason, audit.reason) &&
                Objects.equals(type, audit.type) &&
                Objects.equals(createTime, audit.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creation.getUser().getId(), idea, result, reason, type, createTime);
    }
}
