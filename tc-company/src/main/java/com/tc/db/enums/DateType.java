package com.tc.db.enums;


import com.tc.dto.trans.TransEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看类型
 * @author Cyg
 */
public enum DateType {

    DAY("日"),
    MONTH("月"),
    YEAR("年")

    ;


    private String state;

    public String getState() {
        return state;
    }

    DateType(String state) {
        this.state = state;
    }


    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (DateType dateType : DateType.values()) {
            result.add(TransEnum.init(dateType.name(),dateType.getState()));
        }
        return result;
    }
}
