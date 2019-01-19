package com.tc.dto.audit;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * 审核记录内容
 * @author Cyg
 */
public class AuditContext {

    @NotEmpty
    @Length(max = 32)
    private String id;
    @NotEmpty
    @Length(max = 255)
    private String context;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
