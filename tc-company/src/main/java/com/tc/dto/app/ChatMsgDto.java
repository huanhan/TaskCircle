package com.tc.dto.app;


import java.sql.Timestamp;

public class ChatMsgDto {

    private String title;

    private Long hunterId;
    private Long userId;
    private Long sender;
    private String taskId;
    private Timestamp createTime;
    private String content;
    private String userIcon;
    private String hunterIcon;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getHunterIcon() {
        return hunterIcon;
    }

    public void setHunterIcon(String hunterIcon) {
        this.hunterIcon = hunterIcon;
    }
}
