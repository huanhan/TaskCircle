package com.tc.dto;

import java.sql.Timestamp;

/**
 * 时间范围
 * @author Cyg
 */
public class TimeScop {
    private Timestamp begin;
    private Timestamp end;

    public TimeScop() {
    }

    public TimeScop(Timestamp begin, Timestamp end) {
        this.begin = begin;
        this.end = end;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
}
