package com.tc.db.enums;

import com.tc.dto.condition.ConditionKey;

import java.util.ArrayList;
import java.util.List;

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

    public static List<ConditionKey> toList(){
        List<ConditionKey> result = new ArrayList<>();
        for (UserState userState : UserState.values()) {
            result.add(new ConditionKey(userState.name(),userState.state));
        }
        return result;
    }
}
