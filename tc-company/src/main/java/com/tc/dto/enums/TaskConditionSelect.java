package com.tc.dto.enums;

import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

public enum TaskConditionSelect {
    STATE("任务状态"),
    TYPE("任务类别"),
    CLASSIFY("任务分类"),

    ;
    private String select;

    public String getSelect() {
        return select;
    }

    TaskConditionSelect(String select) {
        this.select = select;
    }

    public static List<TransEnum> toTrans() {
        List<TransEnum> result = new ArrayList<>();
        for (TaskConditionSelect tSelect:
                TaskConditionSelect.values()) {
            result.add(new TransEnum(tSelect.name(),tSelect.getSelect()));
        }
        return result;
    }
}
