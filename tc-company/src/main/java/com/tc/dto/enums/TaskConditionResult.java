package com.tc.dto.enums;

/**
 * 设置统计内容
 * @author Cyg
 */

public enum TaskConditionResult {

    MONEY("任务金额"),
    NUMBER("任务数量"),
    ALL("全部"),

    ;
    private String result;

    TaskConditionResult(String result) {
        this.result = result;
    }

}
