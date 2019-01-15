package com.tc.db.enums;

/**
 * 审核类别
 * @author Cyg
 */

public enum  AuditType {

    HUNTER("猎刃审核"),
    WITHDRAW("提现审核"),
    TASK("新建任务审核"),
    USER_FAILE_TASK("用户放弃任务"),
    HUNTER_FAILE_TASK("猎刃放弃任务"),
    HUNTER_OK_TASK("猎刃完成任务"),
    ;
    private String type;

    AuditType(String type) {
        this.type = type;
    }
}
