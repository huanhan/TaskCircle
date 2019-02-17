package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum MoneyType {
    PAY("支出"),
    INCOME("收入"),
    IS_NULL("什么也没有"),

    ;
    private String type;

    public String getType() {
        return type;
    }

    MoneyType(String type) {
        this.type = type;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (MoneyType value : MoneyType.values()) {
            result.add(TransEnum.init(value.name(),value.getType()));
        }
        return result;
    }
}
