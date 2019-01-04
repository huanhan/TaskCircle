package com.tc.dto.enums;

public enum  QueryEnum {
    AUTHORITY("权限"),
    RESOURCE("url资源")
    ;

    private String state;

    QueryEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
