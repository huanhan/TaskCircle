package com.tc.dto.audit;

import com.tc.db.entity.Audit;
import com.tc.db.entity.AuditHunter;
import com.tc.db.entity.AuditHunterTask;
import com.tc.db.entity.AuditTask;
import com.tc.db.enums.AuditType;
import com.tc.exception.ValidException;
import com.tc.until.IdGenerator;
import com.tc.until.StringResourceCenter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 添加任务审核
 * @author Cyg
 */
public class AddTaskAudit extends BasicAudit{

    @NotEmpty
    @Length(max = 32)
    private String taskId;
    @NotNull
    @Min(0)
    private Float money;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
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
            case TASK:
                audit.setAuditTask(new AuditTask(audit.getId(),taskId,money));
                break;
            case USER_FAILE_TASK:
                audit.setAuditTask(new AuditTask(audit.getId(),taskId,0f));
                break;
            case HUNTER_FAILE_TASK:
                audit.setAuditHunterTask(new AuditHunterTask(audit.getId(),taskId));
                break;
            case HUNTER_OK_TASK:
                audit.setAuditHunterTask(new AuditHunterTask(audit.getId(),taskId));
                break;
            default:
                throw new ValidException(StringResourceCenter.VALIDATOR_INSERT_FAILED);
        }
        return audit;
    }
}
