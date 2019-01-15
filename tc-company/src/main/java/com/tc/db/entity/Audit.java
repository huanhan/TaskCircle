package com.tc.db.entity;

import com.tc.db.enums.AuditState;
import com.tc.db.enums.AuditType;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * 审核记录
 * @author Cyg
 */
@Entity
public class Audit {

    public static final String ID = "id";
    public static final String ADMIN_ID = "adminId";
    public static final String IDEA = "idea";
    public static final String RESULT = "result";
    public static final String REASON = "reason";
    public static final String TYPE = "type";
    public static final String CREATE_TIME = "createTime";
    public static final String AUDIT_HUNTER = "auditHunter";
    public static final String AUDIT_TASK = "auditTask";
    public static final String AUDIT_WITHDRAW = "auditWithdraw";
    public static final String ADMIN = "admin";


    private String id;
    private Long adminId;
    private String idea;
    private AuditState result;
    private String reason;
    private AuditType type;
    private Timestamp createTime;
    private AuditHunter auditHunter;
    private AuditTask auditTask;
    private AuditWithdraw auditWithdraw;
    private Admin admin;
    private AuditHunterTask auditHunterTask;

    public static Audit toDetail(Audit result) {
        if (result != null){
            if (result.getAuditWithdraw() != null){
                result.setAuditWithdraw(new AuditWithdraw(result.getAuditWithdraw().getWithdrawId()));
            }
            if (result.getAuditTask() != null){
                result.setAuditTask(new AuditTask(result.getAuditTask().getTaskId(),result.getAuditTask().getMoney()));
            }
            if (result.getAuditHunter() != null){
                result.setAuditHunter(new AuditHunter(result.getAuditHunter().getUserId()));
            }
        }
        return result;
    }

    /**
     * 权限审核列表
     * @param audits
     * @return
     */
    public static List<Audit> toListInIndex(List<Audit> audits) {
        if (!ListUtils.isEmpty(audits)){
            audits.forEach(audit ->{
                audit.setAuditHunter(null);
                audit.setAdmin(new Admin(audit.getAdminId(),audit.getAdmin().getUser().getName(),audit.getAdmin().getUser().getUsername()));
                audit.setAuditHunter(null);
                audit.setAuditWithdraw(null);
            });
        }
        return audits;
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "admin_id")
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    public AuditState getResult() {
        return result;
    }

    public void setResult(AuditState result) {
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
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Audit audit = (Audit) o;
        return adminId.equals(audit.adminId) &&
                Objects.equals(id, audit.id) &&
                Objects.equals(idea, audit.idea) &&
                Objects.equals(result, audit.result) &&
                Objects.equals(reason, audit.reason) &&
                Objects.equals(type, audit.type) &&
                Objects.equals(createTime, audit.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, adminId, idea, result, reason, type, createTime);
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id", referencedColumnName = "audit_id", nullable = false)
    public AuditHunter getAuditHunter() {
        return auditHunter;
    }

    public void setAuditHunter(AuditHunter auditHunter) {
        this.auditHunter = auditHunter;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id", referencedColumnName = "audit_id", nullable = false)
    public AuditTask getAuditTask() {
        return auditTask;
    }

    public void setAuditTask(AuditTask auditTask) {
        this.auditTask = auditTask;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id", referencedColumnName = "audit_id", nullable = false)
    public AuditWithdraw getAuditWithdraw() {
        return auditWithdraw;
    }

    public void setAuditWithdraw(AuditWithdraw auditWithdraw) {
        this.auditWithdraw = auditWithdraw;
    }

    @ManyToOne
    @JoinColumns(@JoinColumn(name = "admin_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false))
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id", referencedColumnName = "audit_id", nullable = false)
    public AuditHunterTask getAuditHunterTask() {
        return auditHunterTask;
    }

    public void setAuditHunterTask(AuditHunterTask auditHunterTask) {
        this.auditHunterTask = auditHunterTask;
    }
}
