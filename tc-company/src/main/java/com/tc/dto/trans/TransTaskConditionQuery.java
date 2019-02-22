package com.tc.dto.trans;

import com.tc.db.entity.TaskClassify;

import java.io.Serializable;
import java.util.List;

public class TransTaskConditionQuery implements Serializable {
    private List<TransEnum> cResult;
    private List<TransEnum> cSelect;
    private List<TransEnum> taskTypes;
    private List<TransEnum> taskStates;
    private List<TransEnum> taskClassifies;

    public List<TransEnum> getcResult() {
        return cResult;
    }

    public void setcResult(List<TransEnum> cResult) {
        this.cResult = cResult;
    }

    public List<TransEnum> getcSelect() {
        return cSelect;
    }

    public void setcSelect(List<TransEnum> cSelect) {
        this.cSelect = cSelect;
    }

    public List<TransEnum> getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(List<TransEnum> taskTypes) {
        this.taskTypes = taskTypes;
    }

    public List<TransEnum> getTaskStates() {
        return taskStates;
    }

    public void setTaskStates(List<TransEnum> taskStates) {
        this.taskStates = taskStates;
    }

    public List<TransEnum> getTaskClassifies() {
        return taskClassifies;
    }

    public void setTaskClassifies(List<TransEnum> taskClassifies) {
        this.taskClassifies = taskClassifies;
    }
}
