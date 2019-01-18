package com.tc.dto.condition;

import com.tc.db.entity.Condition;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 添加查询条件
 * @author Cyg
 */
public class AddCondition {

    @NotEmpty
    @Length(max = 50)
    private String context;
    @NotEmpty
    @Length(max = 50)
    private String name;
    @NotEmpty
    @Length(max = 50)
    private String value;
    @NotNull
    private Long adminId;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public static Condition toCondition(AddCondition addCondition) {
        Condition condition = new Condition();
        BeanUtils.copyProperties(addCondition,condition);
        condition.setCreationTime(new Timestamp(System.currentTimeMillis()));
        return condition;
    }
}
