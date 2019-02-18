package com.tc.dto.trans;

import java.util.List;

public class TransStates {
    private List<TransEnum> trans;
    private Object data;

    public TransStates() {
    }

    public TransStates(List<TransEnum> trans, Object data) {
        this.trans = trans;
        this.data = data;
    }

    public List<TransEnum> getTrans() {
        return trans;
    }

    public void setTrans(List<TransEnum> trans) {
        this.trans = trans;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
