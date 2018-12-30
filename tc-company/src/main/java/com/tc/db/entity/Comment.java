package com.tc.db.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 评论实体
 * @author Cyg
 */
@Entity
public class Comment {
    private Long id;
    private String type;
    private String context;
    private Timestamp createTime;
    private Double number;
    private Long creationId;
    private CommentHunter commentHunter;
    private CommentTask commentTask;
    private CommentUser commentUser;
    private User creation;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "number")
    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    @Basic
    @Column(name = "creation")
    public Long getCreationId() {
        return creationId;
    }

    public void setCreationId(Long creationId) {
        this.creationId = creationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return id.equals(comment.id) &&
                Double.compare(comment.number, number) == 0 &&
                creationId.equals(comment.creationId) &&
                Objects.equals(type, comment.type) &&
                Objects.equals(context, comment.context) &&
                Objects.equals(createTime, comment.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, type, context, createTime, number, creationId);
    }

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "comment_id", nullable = false)
    public CommentHunter getCommentHunter() {
        return commentHunter;
    }

    public void setCommentHunter(CommentHunter commentHunter) {
        this.commentHunter = commentHunter;
    }

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "comment_id", nullable = false)
    public CommentTask getCommentTask() {
        return commentTask;
    }

    public void setCommentTask(CommentTask commentTask) {
        this.commentTask = commentTask;
    }

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "comment_id", nullable = false)
    public CommentUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(CommentUser commentUser) {
        this.commentUser = commentUser;
    }

    @ManyToOne
    @JoinColumn(name = "creation", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getCreation() {
        return creation;
    }

    public void setCreation(User creation) {
        this.creation = creation;
    }
}
