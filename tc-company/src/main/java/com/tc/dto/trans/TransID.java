package com.tc.dto.trans;

public class TransID {
    private Long key;
    private Long value;

    public TransID() {
    }

    public TransID(Long key, Long value) {
        this.key = key;
        this.value = value;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
