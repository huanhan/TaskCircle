package com.tc.dto.app;

import com.tc.db.entity.Comment;
import com.tc.db.entity.CommentHunter;

import java.sql.Timestamp;

public class CommentHunterDto {
    private Long commentId;
    private String context;
    private Timestamp createTime;
    private Float start;
    private Long hunterId;
    private String name;
    private String hunterTaskId;
    private String img;

    public static CommentHunterDto init(Comment comment) {
        CommentHunterDto commentDto = new CommentHunterDto();
        commentDto.setCommentId(comment.getId());
        commentDto.setContext(comment.getContext());
        commentDto.setCreateTime(comment.getCreateTime());
        commentDto.setHunterId(comment.getCreation().getId());
        commentDto.setHunterTaskId(comment.getCommentHunter().getHunterTaskId());
        commentDto.setName(comment.getCommentHunter().getHunter().getUser().getName());
        commentDto.setStart(comment.getNumber());
        commentDto.setImg(comment.getCommentHunter().getHunter().getUser().getHeadImg());
        return commentDto;
    }

    public static CommentHunterDto init(CommentHunter commentHunter) {
        CommentHunterDto commentDto = new CommentHunterDto();
        commentDto.setCommentId(commentHunter.getComment().getId());
        commentDto.setContext(commentHunter.getComment().getContext());
        commentDto.setCreateTime(commentHunter.getComment().getCreateTime());
        commentDto.setHunterId(commentHunter.getComment().getCreationId());
        commentDto.setHunterTaskId(commentHunter.getHunterTaskId());
        commentDto.setName(commentHunter.getHunter().getUser().getName());
        commentDto.setStart(commentHunter.getComment().getNumber());
        commentDto.setImg(commentHunter.getComment().getCreation().getHeadImg());
        return commentDto;
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

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
