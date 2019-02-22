package com.tc.dto.trans;

import com.tc.db.entity.TaskClassify;

import java.util.List;

public class TransTaskQuery {
    private List<TransEnum> transStates;
    private List<TransEnum> transTypes;
    private List<TaskClassify> classifies;

    public TransTaskQuery() {
    }

    public TransTaskQuery(List<TransEnum> transStates, List<TransEnum> transTypes, List<TaskClassify> classifies) {
        this.transStates = transStates;
        this.transTypes = transTypes;
        this.classifies = classifies;
    }

    public List<TransEnum> getTransStates() {
        return transStates;
    }

    public void setTransStates(List<TransEnum> transStates) {
        this.transStates = transStates;
    }

    public List<TransEnum> getTransTypes() {
        return transTypes;
    }

    public void setTransTypes(List<TransEnum> transTypes) {
        this.transTypes = transTypes;
    }

    public List<TaskClassify> getClassifies() {
        return classifies;
    }

    public void setClassifies(List<TaskClassify> classifies) {
        this.classifies = classifies;
    }
}
