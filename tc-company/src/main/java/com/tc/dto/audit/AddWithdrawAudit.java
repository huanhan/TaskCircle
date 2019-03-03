package com.tc.dto.audit;

import com.tc.db.entity.Audit;
import com.tc.db.entity.AuditWithdraw;
import com.tc.db.enums.AuditType;
import com.tc.until.IdGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 添加提现审核
 * @author Cyg
 */
public class AddWithdrawAudit extends BasicAudit{

    @NotEmpty
    @Length(max = 32)
    private String withdrawId;

    @NotNull
    private Float adminMoney;

    private Float userMoney;

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    public Float getAdminMoney() {
        return adminMoney;
    }

    public void setAdminMoney(Float adminMoney) {
        this.adminMoney = adminMoney;
    }

    public Float getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(Float userMoney) {
        this.userMoney = userMoney;
    }

    @Override
    public Audit toAudit() {
        Audit audit = new Audit();
        audit.setId(IdGenerator.INSTANCE.nextId());
        audit.setAdminId(getAdminId());
        audit.setIdea(getIdea());
        audit.setReason(getReason());
        audit.setResult(getResult());
        audit.setType(AuditType.WITHDRAW);
        audit.setAuditWithdraw(new AuditWithdraw(audit.getId(),withdrawId,adminMoney,userMoney));
        return audit;
    }
}
