package com.tc.dto.app;

import com.tc.db.entity.Audit;
import com.tc.db.enums.AuditState;
import com.tc.db.enums.AuditType;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

public class AuditDto {

    private String id;
    private Long adminId;
    private String idea;
    private AuditState result;
    private String reason;
    private AuditType type;
    private Timestamp createTime;
    private String auditerName;

    public static AuditDto toDetail(Audit auditTask) {
        AuditDto auditDto = new AuditDto();
        auditDto.setAuditerName(auditTask.getAdmin().getUser().getName());
        BeanUtils.copyProperties(auditTask,auditDto);
        return auditDto;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getAuditerName() {
        return auditerName;
    }

    public void setAuditerName(String auditerName) {
        this.auditerName = auditerName;
    }

}
