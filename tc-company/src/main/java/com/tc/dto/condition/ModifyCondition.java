package com.tc.dto.condition;

import com.tc.db.entity.Condition;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 修改查询条件
 * @author Cyg
 */
public class ModifyCondition {

    @NotNull
    @Min(1)
    private Long id;
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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public static boolean toCondition(Condition condition, ModifyCondition modifyCondition) {
        boolean isUpdate = false;
        if (!modifyCondition.context.equals(condition.getContext())){
            condition.setContext(modifyCondition.context);
            isUpdate = true;
        }
        if (!modifyCondition.name.equals(condition.getName())){
            condition.setName(modifyCondition.name);
            isUpdate = true;
        }
        if (!modifyCondition.value.equals(condition.getValue())){
            condition.setValue(modifyCondition.value);
            isUpdate = true;
        }
        return isUpdate;

    }
}
