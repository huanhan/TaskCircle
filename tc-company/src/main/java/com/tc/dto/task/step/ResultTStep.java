package com.tc.dto.task.step;

import com.tc.db.entity.TaskStep;

import java.util.List;

public class ResultTStep {
    private List<TaskStep> taskSteps;
    private List<TransHT> htSteps;

    public ResultTStep() {
    }

    public ResultTStep(List<TaskStep> taskSteps, List<TransHT> htSteps) {
        this.taskSteps = taskSteps;
        this.htSteps = htSteps;
    }

    public List<TaskStep> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(List<TaskStep> taskSteps) {
        this.taskSteps = taskSteps;
    }

    public List<TransHT> getHtSteps() {
        return htSteps;
    }

    public void setHtSteps(List<TransHT> htSteps) {
        this.htSteps = htSteps;
    }
}
