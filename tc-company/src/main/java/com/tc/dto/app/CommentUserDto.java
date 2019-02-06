package com.tc.dto.app;

import com.tc.db.entity.CommentUser;

import java.sql.Timestamp;

public class CommentUserDto {
    private String context;
    private Timestamp createTime;
    private Float start;
    private Long hunterId;
    private String name;

    private String taskId;

    public static CommentUserDto init(CommentUser commentUser) {
        CommentUserDto commentUserDto = new CommentUserDto();
        commentUserDto.setContext(commentUser.getComment().getContext());
        commentUserDto.setCreateTime(commentUser.getComment().getCreateTime());
        commentUserDto.setHunterId(commentUser.getComment().getCreation().getId());
        commentUserDto.setTaskId(commentUser.getTaskId());
        commentUserDto.setName(commentUser.getUser().getName());
        commentUserDto.setStart(commentUser.getComment().getNumber());
        return commentUserDto;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Float getStart() {
        return start;
    }

    public void setStart(Float start) {
        this.start = start;
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
