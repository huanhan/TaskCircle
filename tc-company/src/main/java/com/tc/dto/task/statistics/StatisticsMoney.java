package com.tc.dto.task.statistics;

public class StatisticsMoney extends StatisticsBasic{
    private Float money;

    public StatisticsMoney() {
    }

    public StatisticsMoney(Float money) {
        this.money = money;
    }

    public StatisticsMoney(String key, Float money) {
        super(key);
        this.money = money;
    }

    public StatisticsMoney(String key, String name, Float money) {
        super(key, name);
        this.money = money;
    }



    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }
}
