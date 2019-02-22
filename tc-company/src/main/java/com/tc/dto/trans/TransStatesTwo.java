package com.tc.dto.trans;

import java.util.List;

public class TransStatesTwo {
    private List<TransEnum> transStates;
    private List<TransEnum> transTypes;
    private Object data;
    private Trans trans;

    public TransStatesTwo() {
    }

    public TransStatesTwo(List<TransEnum> transStates, List<TransEnum> transTypes, Object data,Trans trans) {
        this.transStates = transStates;
        this.transTypes = transTypes;
        this.data = data;
        this.trans = trans;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }
}
