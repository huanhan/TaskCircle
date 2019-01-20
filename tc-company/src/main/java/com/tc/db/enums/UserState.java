package com.tc.db.enums;

/**
 * 用户状态
 */
public enum  UserState {
    NORMAL("一般"),
    AUDIT_HUNTER("猎刃审核"),
    AUDIT_CENTER("审核中"),
    AUDIT_FAILE("审核失败"),
    AUDIT_SUCCESS("审核成功")

    ;
    private String state;

    UserState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }


}
