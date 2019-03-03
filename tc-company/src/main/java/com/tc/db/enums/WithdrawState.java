package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum WithdrawState {
    SUCCESS("提现成功"),
    PAY_SUCCESS("充值成功"),
    FAILED("提现失败"),
    PAY_FAILED("充值失败"),
    EXCEPTION("提现异常"),
    PAY_EXCEPTION("充值异常"),
    AUDIT("提现审核"),
    PAY_AUDIT("充值审核"),
    AUDIT_CENTER("提现审核中"),
    PAY_AUDIT_CENTER("充值审核中");
    private String state;

    public static boolean isAudit(WithdrawState state) {
        switch (state) {
            case AUDIT:
                return true;
            case PAY_AUDIT:
                return true;
            case AUDIT_CENTER:
                return true;
            case PAY_AUDIT_CENTER:
                return true;
            default:
                return false;
        }
    }

    public static List<WithdrawState> getAudit() {
        List<WithdrawState> result = new ArrayList<>();
        result.add(AUDIT);
        result.add(PAY_AUDIT);
        result.add(AUDIT_CENTER);
        result.add(PAY_AUDIT_CENTER);
        return result;

    }

    /**
     * 判断 收支状态 是否处在审核中
     * @param state
     * @return
     */
    public static boolean isAuditCenter(WithdrawState state) {
        switch (state) {
            case AUDIT_CENTER:
                return true;
            case AUDIT:
                return true;
            default:
                return false;
        }
    }


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
     *
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
