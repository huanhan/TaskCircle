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

    public static DateType find(String type) {
        try {
            return DateType.valueOf(type);
        }catch (Exception e){
            return null;
        }
    }

    public String getType() {
        return type;
    }

    DateType(String type) {
        this.type = type;
    }
}
