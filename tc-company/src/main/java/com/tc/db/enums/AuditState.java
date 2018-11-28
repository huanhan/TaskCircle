package com.tc.db.enums;

public enum AuditState {

    WAIT_ADUIT("待审核"),
    ADUIT("审核中"),
    PASS("通过"),
    NO_PASS("不通过")
    ;


    private String state;

    AuditState(String state) {
        this.state = state;
    }
}
