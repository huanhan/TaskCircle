package com.tc.dto.app;

import java.sql.Timestamp;

public class ChatMsgDto {

    private String content;
    private String userId;
    private String hunterId;
    private String taskId;
    private Timestamp time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHunterId() {
        return hunterId;
    }

    public void setHunterId(String hunterId) {
        this.hunterId = hunterId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
