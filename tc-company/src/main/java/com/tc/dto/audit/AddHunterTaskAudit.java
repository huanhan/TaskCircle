package com.tc.dto.audit;

import com.tc.db.entity.Audit;
import com.tc.db.entity.AuditHunterTask;
import com.tc.exception.ValidException;
import com.tc.until.IdGenerator;
import com.tc.until.StringResourceCenter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 添加猎刃任务审核结果
 * @author Cyg
 */
public class AddHunterTaskAudit extends BasicAudit {

    @NotEmpty
    @Length(max = 32)
    private String htId;

    public String getHtId() {
        return htId;
    }

    public void setHtId(String htId) {
        this.htId = htId;
    }

    @Override
    public Audit toAudit() {
        Audit audit = new Audit();
        audit.setId(IdGenerator.INSTANCE.nextId());
        audit.setAdminId(getAdminId());
        audit.setIdea(getIdea());
        audit.setReason(getReason());
        audit.setResult(getResult());
        audit.setType(getType());
        switch (getType()){
            case HUNTER_FAILURE_TASK:
                audit.setAuditHunterTask(new AuditHunterTask(audit.getId(),htId));
                break;
            case HUNTER_OK_TASK:
                audit.setAuditHunterTask(new AuditHunterTask(audit.getId(),htId));
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_INSERT_FAILED);
        }
        return audit;
    }
}
