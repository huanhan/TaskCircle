package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum  WithdrawType {
    PAY("充值"),
    WITHDRAW("提现"),
            ;
    private String type;

    public String getType() {
        return type;
    }

    WithdrawType(String type) {
        this.type = type;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (WithdrawType value : WithdrawType.values()) {
            result.add(TransEnum.init(value.name(),value.getType()));
        }
        return result;
    }
}
