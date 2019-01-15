package com.tc.dto.audit;

import com.tc.db.entity.Audit;
import com.tc.db.entity.AuditHunter;
import com.tc.db.enums.AuditType;
import com.tc.until.IdGenerator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加用户审核
 * @author Cyg
 */
public class AddHunterAudit extends BasicAudit{
    @NotNull
    @Min(value = 1)
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Audit toAudit() {
        Audit audit = new Audit();
        audit.setId(IdGenerator.INSTANCE.nextId());
        audit.setAdminId(getAdminId());
        audit.setIdea(getIdea());
        audit.setReason(getReason());
        audit.setResult(getResult());
        audit.setType(AuditType.HUNTER);
        audit.setAuditHunter(new AuditHunter(audit.getId(),userId));
        return audit;
    }
}
