package com.tc.db.enums;

public enum MessageType {
    CONDITION("条件型"),
    ALL("所有型"),
    APPOINT("指定型"),

    ;

    private String type;

    MessageType(String type) {
        this.type = type;
    }
}
