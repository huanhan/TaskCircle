package com.tc.db.enums;

public enum  WithdrawState {
    SUCCESS("提现成功"),
    FAILED("提现失败"),
    EXCEPTION("提现异常"),
    AUDIT("提现审核"),
    ;
    private String state;

    WithdrawState(String state) {
        this.state = state;
    }
}
