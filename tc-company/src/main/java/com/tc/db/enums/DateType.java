package com.tc.db.enums;

import java.sql.Timestamp;

public enum DateType {

    DAY("日"),
    MONTH("月"),
    YEAR("年")

    ;


    private String state;

    DateType(String state) {
        this.state = state;
    }


}
