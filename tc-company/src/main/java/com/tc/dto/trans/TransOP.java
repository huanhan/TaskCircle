package com.tc.dto.trans;

import java.util.List;

public class TransOP extends Trans {
    private Boolean isManager;
    private List<TransOP> children;

    public TransOP() {
    }

    public TransOP(Boolean isManager) {
        this.isManager = isManager;
    }

    public TransOP(Object key, String value, Boolean isManager) {
        super(key, value);
        this.isManager = isManager;
    }

    public TransOP(Object key, String value, Boolean isManager, List<TransOP> children) {
        super(key, value);
        this.isManager = isManager;
        this.children = children;
    }

    public TransOP(Object key, Boolean isManager) {
        super(key);
        this.isManager = isManager;
    }

    public Boolean getManager() {
        return isManager;
    }

    public void setManager(Boolean manager) {
        isManager = manager;
    }

    public List<TransOP> getChildren() {
        return children;
    }

    public void setChildren(List<TransOP> children) {
        this.children = children;
    }
}
