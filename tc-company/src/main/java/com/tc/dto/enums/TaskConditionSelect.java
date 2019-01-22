package com.tc.dto.enums;

public enum TaskConditionSelect {
    STATE("任务状态"),
    TYPE("任务类别"),
    CLASSIFY("任务分类"),

    ;
    private String select;

    TaskConditionSelect(String select) {
        this.select = select;
    }
}
