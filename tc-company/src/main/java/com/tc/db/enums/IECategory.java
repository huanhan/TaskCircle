package com.tc.db.enums;

/**
 * @author Cyg
 * 收付款类别
 */

public enum IECategory{

    INCOME("收入"),
    EXPENSES("支出")

    ;

    private String category;

    IECategory(String category) {
        this.category = category;
    }
}
