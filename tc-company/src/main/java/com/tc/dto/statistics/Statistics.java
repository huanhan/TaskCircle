package com.tc.dto.statistics;

import java.sql.Timestamp;

public class Statistics {
    private Timestamp key;
    private Float value;

    public Statistics() {
    }

    public Statistics(Timestamp key, Float value) {
        this.key = key;
        this.value = value;
    }

    public Timestamp getKey() {
        return key;
    }

    public void setKey(Timestamp key) {
        this.key = key;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public static Statistics init(Timestamp key,Float value){
        return new Statistics(key,value);
    }
}
