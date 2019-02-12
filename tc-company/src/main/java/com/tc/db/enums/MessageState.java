package com.tc.db.enums;

import com.tc.dto.TransEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息状态
 * @author Cyg
 */

public enum  MessageState {

    NORMAL("正常"),
    STOP("停用"),

    ;
    private String state;

    public String getState() {
        return state;
    }

    MessageState(String state) {
        this.state = state;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (MessageState value : MessageState.values()) {
            result.add(TransEnum.init(value.name(),value.getState()));
        }
        return result;
    }
}
