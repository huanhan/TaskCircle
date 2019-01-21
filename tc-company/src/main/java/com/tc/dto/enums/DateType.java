package com.tc.dto.enums;

/**
 * 日期类别
 * @author Cyg
 */

public enum DateType {

    HOURS("小时"),
    DAY("日"),
    MONTH("月"),
    YEAR("年"),


    ;
    private String type;

    DateType(String type) {
        this.type = type;
    }
}
