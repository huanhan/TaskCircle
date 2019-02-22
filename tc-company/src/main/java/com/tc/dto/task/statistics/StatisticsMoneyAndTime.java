package com.tc.dto.task.statistics;

import java.sql.Timestamp;

public class StatisticsMoneyAndTime {
    private Timestamp key;
    private Float money;

    public StatisticsMoneyAndTime() {
    }

    public StatisticsMoneyAndTime(Timestamp key, Float money) {
        this.key = key;
        this.money = money;
    }

    public Timestamp getKey() {
        return key;
    }

    public void setKey(Timestamp key) {
        this.key = key;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }
}
