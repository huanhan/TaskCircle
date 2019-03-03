package com.tc.dto.huntertask;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;

import java.util.List;

public class HTStepResult {
    private HunterTask hunterTask;
    private List<HunterTaskStep> steps;

    public HTStepResult() {
    }

    public HTStepResult(HunterTask hunterTask, List<HunterTaskStep> steps) {
        this.hunterTask = hunterTask;
        this.steps = steps;
    }

    public HunterTask getHunterTask() {
        return hunterTask;
    }

    public void setHunterTask(HunterTask hunterTask) {
        this.hunterTask = hunterTask;
    }

    public List<HunterTaskStep> getSteps() {
        return steps;
    }

    public void setSteps(List<HunterTaskStep> steps) {
        this.steps = steps;
    }
}
