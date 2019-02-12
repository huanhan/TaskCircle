package com.tc.db.enums;

import com.tc.dto.TransEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyg
 * 收付款类别
 */

public enum IECategory{

    INCOME("收入"),
    EXPENSES("支出")

    ;

    private String category;

    public String getCategory() {
        return category;
    }

    IECategory(String category) {
        this.category = category;
    }

    public static List<TransEnum> toList() {
        List<TransEnum> result = new ArrayList<>();
        for (IECategory value : IECategory.values()) {
            result.add(TransEnum.init(value.name(),value.getCategory()));
        }
        return result;

    }
}
