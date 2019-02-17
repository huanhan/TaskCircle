package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum  WithdrawState {
    SUCCESS("提现成功"),
    FAILED("提现失败"),
    EXCEPTION("提现异常"),
    AUDIT("提现审核"),
    AUDIT_CENTER("审核中")
    ;
    private String state;

    public String getState() {
        return state;
    }

    WithdrawState(String state) {
        this.state = state;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (WithdrawState value : WithdrawState.values()) {
            result.add(TransEnum.init(value.name(),value.getState()));
        }
        return result;
    }
}
