package com.tc.dto.huntertask;

import com.tc.db.entity.HunterTaskStep;
import com.tc.until.TimestampHelper;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加猎刃任务步骤
 * @author Cyg
 */
public class AddHunterTaskStep {
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

    public static HunterTaskStep toHunterTaskStep(AddHunterTaskStep addHunterTaskStep) {
        HunterTaskStep hunterTaskStep = new HunterTaskStep();
        BeanUtils.copyProperties(addHunterTaskStep,hunterTaskStep);
        hunterTaskStep.setFinishTime(TimestampHelper.today());
        return hunterTaskStep;
    }
}
