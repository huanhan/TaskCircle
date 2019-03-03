package com.tc.dto.task.step;

import com.tc.dto.trans.TransData;

import java.util.List;

public class TransHT {
    private Long userId;
    private String username;
    private String name;
    private String htId;

    private List<TransData> steps;


    public TransHT() {
    }

    public TransHT(Long userId, String username, String name, String htId, List<TransData> steps) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.htId = htId;
        this.steps = steps;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtId() {
        return htId;
    }

    public void setHtId(String htId) {
        this.htId = htId;
    }

    public List<TransData> getSteps() {
        return steps;
    }

    public void setSteps(List<TransData> steps) {
        this.steps = steps;
    }
}
