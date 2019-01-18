package com.tc.dto.huntertask;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Cyg
 */
public class DeleteHunterTaskStep {

    @NotEmpty
    @Length(max = 32)
    private String hunterTaskId;
    @NotNull
    @Min(1)
    private Integer step;

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}
