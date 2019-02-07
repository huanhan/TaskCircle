package com.tc.dto.app;

import com.tc.db.entity.Comment;
import com.tc.db.entity.CommentUser;

import java.sql.Timestamp;

public class CommentUserDto {
    private Long commentId;
    private String context;
    private Timestamp createTime;
    private Float start;
    private Long hunterId;
    private Long userId;
    private String name;

    private String taskId;

    public static CommentUserDto init(CommentUser commentUser) {
        CommentUserDto commentUserDto = new CommentUserDto();
        commentUserDto.setCommentId(commentUser.getCommentId());
        commentUserDto.setContext(commentUser.getComment().getContext());
        commentUserDto.setCreateTime(commentUser.getComment().getCreateTime());
        commentUserDto.setHunterId(commentUser.getComment().getCreation().getId());
        commentUserDto.setUserId(commentUser.getUserId());
        commentUserDto.setTaskId(commentUser.getTaskId());
        commentUserDto.setName(commentUser.getUser().getName());
        commentUserDto.setStart(commentUser.getComment().getNumber());
        return commentUserDto;
    }

    public static CommentUserDto init(Comment comment) {
        CommentUserDto commentUserDto = new CommentUserDto();
        commentUserDto.setCommentId(comment.getId());
        commentUserDto.setContext(comment.getContext());
        commentUserDto.setCreateTime(comment.getCreateTime());
        commentUserDto.setHunterId(comment.getCreationId());
        commentUserDto.setUserId(comment.getCommentUser().getUserId());
        commentUserDto.setTaskId(comment.getCommentUser().getTaskId());
        commentUserDto.setName(comment.getCommentUser().getUser().getName());
        commentUserDto.setStart(comment.getNumber());
        return commentUserDto;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
