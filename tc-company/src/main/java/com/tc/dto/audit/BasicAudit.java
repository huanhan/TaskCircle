package com.tc.dto.audit;

import com.tc.db.entity.Audit;
import com.tc.db.enums.AuditState;
import com.tc.db.enums.AuditType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 审核结果的基本信息
 * @author Cyg
 */
public abstract class BasicAudit {
    private Long adminId;
    @NotEmpty
    @Length(max = 100)
    private String idea;
    @NotNull
    private AuditState result;
    @NotEmpty
    @Length(max = 100)
    private String reason;
    private AuditType type;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public AuditState getResult() {
        return result;
    }

    public void setResult(AuditState result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AuditType getType() {
        return type;
    }

    public void setType(AuditType type) {
        this.type = type;
    }

    /**
     * 转换成审核实体
     * @return
     */
    public abstract Audit toAudit();
}
