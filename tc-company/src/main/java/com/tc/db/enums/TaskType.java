package com.tc.db.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum  TaskType {

    /**
     * 只允许以个人接
     */
    SOLO("单人型"),

    /**
     * 可以多个人接
     */
    MULTI("多人型"),

    /**
     * 用户指定某个人去完成
     */
    APPOINT("指定型"),

    /**
     * 用户未设置
     */
    NOT("未设置"),
    ;

    private String type;

    TaskType(String type) {
        this.type = type;
    }

    public static List<TaskType> byStrs(List<String> items) {
        List<TaskType> result = new ArrayList<>();
        items.forEach(s -> result.add(TaskType.valueOf(s)));
        return result;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (TaskType value : TaskType.values()) {
            result.add(TransEnum.init(value.name(),value.getType()));
        }
        return result;
    }

    public String getType() {
        return type;
    }
}
