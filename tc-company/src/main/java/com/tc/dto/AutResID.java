package com.tc.dto;

import java.io.Serializable;

public class AutResID implements Serializable {
    private Long autId;
    private Long resId;

    public AutResID() {
    }

    public AutResID(Long autId, Long resId) {
        this.autId = autId;
        this.resId = resId;
    }

    public Long getAutId() {
        return autId;
    }

    public void setAutId(Long autId) {
        this.autId = autId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }
}
