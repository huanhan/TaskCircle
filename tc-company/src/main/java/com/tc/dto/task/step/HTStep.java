package com.tc.dto.task.step;

import com.tc.dto.trans.Trans;

/**
 * 猎刃任务步骤信息
 * @author Cyg
 */
public class HTStep {

    private Long userId;
    private String username;
    private String name;
    private String htId;
    private Integer step;
    private Trans state;

    public HTStep() {
    }

    public HTStep(Long userId, String username, String name, String htId, Integer step, Trans state) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.htId = htId;
        this.step = step;
        this.state = state;
    }


    public HTStep(Long userId, String username, String name, Trans state) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.state = state;
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

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Trans getState() {
        return state;
    }

    public void setState(Trans state) {
        this.state = state;
    }
}
