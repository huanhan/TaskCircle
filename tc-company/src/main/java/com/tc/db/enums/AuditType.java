package com.tc.db.enums;

/**
 * 审核类别
 * @author Cyg
 */

public enum  AuditType {

    HUNTER("猎刃审核"),
    WITHDRAW("提现审核"),
    TASK("任务审核")

    ;
    private String type;

    AuditType(String type) {
        this.type = type;
    }
}
