package com.tc.db.enums;

public enum  MessageState {

    NORAML("正常"),
    STOP("停用"),

    ;
    private String state;

    MessageState(String state) {
        this.state = state;
    }
}
