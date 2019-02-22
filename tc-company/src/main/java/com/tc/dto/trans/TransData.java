package com.tc.dto.trans;

public class TransData extends Trans {

    private Object data;

    public TransData() {
    }

    public TransData(Object data) {
        this.data = data;
    }

    public TransData(Object key, String value, Object data) {
        super(key, value);
        this.data = data;
    }

    public TransData(Object key, Object data) {
        super(key);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
