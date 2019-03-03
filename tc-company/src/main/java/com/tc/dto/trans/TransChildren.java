package com.tc.dto.trans;

import java.util.List;

public class TransChildren extends Trans {
    private List<TransChildren> children;

    public TransChildren() {
    }

    public TransChildren(List<TransChildren> children) {
        this.children = children;
    }

    public TransChildren(Object key, String value, List<TransChildren> children) {
        super(key, value);
        this.children = children;
    }

    public TransChildren(Object key, List<TransChildren> children) {
        super(key);
        this.children = children;
    }

    public List<TransChildren> getChildren() {
        return children;
    }

    public void setChildren(List<TransChildren> children) {
        this.children = children;
    }
}
