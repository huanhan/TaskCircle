package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户状态
 */
public enum UserState {
    NORMAL("一般"),
    AUDIT_HUNTER("猎刃审核"),
    AUDIT_CENTER("审核中"),
    AUDIT_FAILE("审核失败"),
    AUDIT_SUCCESS("审核成功"),
    STOP("停用"),
    DELETE("删除"),;
    private String state;

    UserState(String state) {
        this.state = state;
    }

    public static List<TransEnum> toList() {

        List<TransEnum> result = new ArrayList<>();
        for (UserState value : UserState.values()) {
            result.add(TransEnum.init(value.name(), value.getState()));
        }
        return result;
    }

    public static boolean isAudit(UserState state) {
        switch (state) {
            case AUDIT_HUNTER:
                return true;
            case AUDIT_CENTER:
                return true;
            default:
                return false;
        }
    }

    public static List<UserState> getAudit() {
        List<UserState> result = new ArrayList<>();
        result.add(AUDIT_HUNTER);
        result.add(AUDIT_CENTER);
        return result;
    }

    public String getState() {
        return state;
    }


}
