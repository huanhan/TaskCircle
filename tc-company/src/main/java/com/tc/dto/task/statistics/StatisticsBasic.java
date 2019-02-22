package com.tc.dto.task.statistics;

public class StatisticsBasic {
    private String key;
    private String name;

    public StatisticsBasic() {
    }

    public StatisticsBasic(String key) {
        this.key = key;
    }

    public StatisticsBasic(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
