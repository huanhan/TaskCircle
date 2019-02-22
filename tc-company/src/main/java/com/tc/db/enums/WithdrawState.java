package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum WithdrawState {
    SUCCESS("提现成功"),
    FAILED("提现失败"),
    EXCEPTION("提现异常"),
    AUDIT("提现审核"),
    AUDIT_CENTER("审核中");
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
            result.add(TransEnum.init(value.name(), value.getState()));
        }
        return result;
    }

    public static List<TransEnum> toUserTransList() {
        List<TransEnum> result = new ArrayList<>();
        for (WithdrawState value : WithdrawState.values()
                ) {
            if (!value.name().equals(WithdrawState.AUDIT_CENTER.name()) || !value.name().equals(WithdrawState.AUDIT.name())) {
                result.add(new TransEnum(value.name(), value.state));
            }
        }
        return result;
    }

    public static List<WithdrawState> toUserList() {
        List<WithdrawState> result = new ArrayList<>();
        for (WithdrawState value : WithdrawState.values()
                ) {
            if (!value.name().equals(WithdrawState.AUDIT_CENTER.name()) || !value.name().equals(WithdrawState.AUDIT.name())) {
                result.add(value);
            }
        }
        return result;
    }

    /**
     * 在详情中不允许查看的类别
     * @param state
     * @return
     */
    public static boolean inNotDetail(WithdrawState state) {
        switch (state) {
            case AUDIT:
                return true;
            case AUDIT_CENTER:
                return true;
            default:
                return false;
        }
    }
}
