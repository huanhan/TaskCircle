package com.tc.dto.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

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

    public static List<TransEnum> toTrans() {
        List<TransEnum> result = new ArrayList<>();
        for (TaskConditionResult tResult:
             TaskConditionResult.values()) {
            result.add(new TransEnum(tResult.name(),tResult.getResult()));
        }
        return result;
    }

    public String getResult() {
        return result;
    }

    TaskConditionResult(String result) {
        this.result = result;
    }


}
