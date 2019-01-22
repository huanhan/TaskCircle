package com.tc.db.enums;

/**
 * 消息状态
 * @author Cyg
 */

public enum  MessageState {

    NORMAL("正常"),
    STOP("停用"),

    ;
    private String state;

    MessageState(String state) {
        this.state = state;
    }
}
