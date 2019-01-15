package com.tc.db.enums;

public enum  WithdrawType {
    PAY("充值"),
    WITHDRAW("提现"),
            ;
    private String type;

    WithdrawType(String type) {
        this.type = type;
    }
}
