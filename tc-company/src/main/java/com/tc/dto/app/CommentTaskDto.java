package com.tc.dto.app;

import com.tc.db.entity.CommentTask;

import java.sql.Timestamp;

public class CommentTaskDto {
    private Long commentId;
    private String context;
    private Timestamp createTime;
    private Float start;
    private Long hunterId;
    private String name;
    private String img;

    public static CommentTaskDto init(CommentTask commentTask) {
        CommentTaskDto commentTaskDto=new CommentTaskDto();
        commentTaskDto.setCommentId(commentTask.getCommentId());
        commentTaskDto.setContext(commentTask.getComment().getContext());
        commentTaskDto.setCreateTime(commentTask.getComment().getCreateTime());
        commentTaskDto.setHunterId(commentTask.getComment().getCreation().getId());
        commentTaskDto.setName(commentTask.getComment().getCreation().getName());
        commentTaskDto.setStart(commentTask.getComment().getNumber());
        commentTaskDto.setImg(commentTask.getComment().getCreation().getHeadImg());
        return commentTaskDto;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
