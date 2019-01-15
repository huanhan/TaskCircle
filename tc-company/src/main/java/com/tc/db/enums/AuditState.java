package com.tc.db.enums;

public enum AuditState {

    PASS("通过"),
    NO_PASS("不通过"),
    ARAHC("允许重做，放弃要补偿"),
    ARANC("允许重做，放弃不用补偿"),
    NRNC("不能重做，不用补偿"),
    NRHC("不能重做，要补偿"),
    ;


    private String state;

    AuditState(String state) {
        this.state = state;
    }
}
