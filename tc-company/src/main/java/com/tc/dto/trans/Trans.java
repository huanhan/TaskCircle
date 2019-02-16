package com.tc.dto.trans;

public class Trans {
    private Object key;
    private String value;

    public Trans() {
    }

    public Trans(Object key, String value) {
        this.key = key;
        this.value = value;
    }

    public Trans(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
