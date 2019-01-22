package com.tc.dto.statistics;

public class TaskStatistics {
    private String key;
    private Float money;
    private Long count;
    private String name;

    public TaskStatistics() {
    }

    public TaskStatistics(String key, Long count, String name) {
        this.key = key;
        this.count = count;
        this.name = name;
    }

    public TaskStatistics(String key, Float money, String name) {
        this.key = key;
        this.money = money;
        this.name = name;
    }

    public TaskStatistics(String key, Float money, Long count, String name) {
        this.key = key;
        this.money = money;
        this.count = count;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
