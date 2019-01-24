package com.tc.dto;

import com.tc.until.PageRequest;

import java.sql.Timestamp;

/**
 * 时间范围
 * @author Cyg
 */
public class TimeScope extends PageRequest {
    private Long id;
    private Timestamp begin;
    private Timestamp end;

    public TimeScope() {
        super(0,10);
    }

    public TimeScope(Timestamp begin, Timestamp end) {
        super(0,10);
        this.begin = begin;
        this.end = end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
