package com.tc.dto.enums;

public enum CashType {
    TASK("任务"),
    AUDIT("审核"),
    HUNTER_TASK("猎刃任务")
    ;

    private String type;

    public String getType() {
        return type;
    }

    CashType(String type) {
        this.type = type;
    }
}
