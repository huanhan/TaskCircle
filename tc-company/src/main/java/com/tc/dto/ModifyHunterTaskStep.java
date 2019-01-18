package com.tc.dto;

import com.tc.db.entity.HunterTaskStep;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 修改猎刃步骤
 * @author Cyg
 */
public class ModifyHunterTaskStep {
    @NotEmpty
    @Length(max = 32)
    private String id;
    @NotNull
    @Min(1)
    private Integer step;
    @NotEmpty
    @Length(max = 255)
    private String context;
    @Length(max = 100)
    private String remake;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public static boolean isUpdate(HunterTaskStep hunterTaskStep, ModifyHunterTaskStep modifyHunterTaskStep) {
        boolean isUpdate = false;
        if (!hunterTaskStep.getContext().equals(modifyHunterTaskStep.context)){
            hunterTaskStep.setContext(modifyHunterTaskStep.context);
            isUpdate = true;
        }
        if (!hunterTaskStep.getRemake().equals(modifyHunterTaskStep.remake)){
            hunterTaskStep.setRemake(modifyHunterTaskStep.remake);
            isUpdate = true;
        }
        return isUpdate;
    }
}
