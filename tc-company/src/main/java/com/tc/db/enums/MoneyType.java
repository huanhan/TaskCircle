package com.tc.db.enums;

public enum MoneyType {
    PAY("支出"),
    INCOME("收入"),
    IS_NULL("什么也没有"),

    ;
    private String type;

    MoneyType(String type) {
        this.type = type;
    }

}
