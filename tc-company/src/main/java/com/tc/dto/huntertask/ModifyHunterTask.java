package com.tc.dto.huntertask;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 修改猎刃任务
 * @author Cyg
 */
public class ModifyHunterTask {
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
