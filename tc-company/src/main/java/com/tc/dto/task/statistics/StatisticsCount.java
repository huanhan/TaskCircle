package com.tc.dto.task.statistics;

public class StatisticsCount extends StatisticsBasic {

    private Long count;

    public StatisticsCount() {
    }

    public StatisticsCount(Long count) {
        this.count = count;
    }

    public StatisticsCount(String key, String name, Long count) {
        super(key, name);
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
