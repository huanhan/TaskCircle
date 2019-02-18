package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum MessageType {
    CONDITION("条件型"),
    ALL("所有型"),
    APPOINT("指定型"),

    ;

    private String type;

    public String getType() {
        return type;
    }

    MessageType(String type) {
        this.type = type;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (MessageType value : MessageType.values()) {
            result.add(TransEnum.init(value.name(),value.getType()));
        }
        return result;
    }
}
