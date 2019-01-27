package com.tc.dto.app;


import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

public class HunterTaskStepAppDto {
    private String hunterTaskId;
    private Integer step;
    private Timestamp finishTime;
    private String context;
    private String remake;

    public static HunterTaskStepAppDto toDetail(HunterTaskStep hunterTaskStep) {
        HunterTaskStepAppDto hunterTaskStepAppDto = new HunterTaskStepAppDto();
        BeanUtils.copyProperties(hunterTaskStep, hunterTaskStepAppDto);
        return hunterTaskStepAppDto;
    }

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

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
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
}
