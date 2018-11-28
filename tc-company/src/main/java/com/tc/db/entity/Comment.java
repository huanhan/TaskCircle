package com.tc.db.entity;

import com.tc.db.enums.CommentType;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Cyg
 * 评论记录
 */
@Entity
public class Comment implements Serializable {
    private Long id;
    private CommentType type;
    private String context;
    private Timestamp createTime;
    private Float number;
    private User creation;
    private CommentHunter commentHunter;
    private CommentTask commentTask;
    private CommentUser commentUser;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public CommentType getType() {
        return type;
    }

    public void setType(CommentType type) {
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
    public Float getNumber() {
        return number;
    }

    public void setNumber(Float number) {
        this.number = number;
    }



    @ManyToOne
    @JoinColumn(name = "creation", referencedColumnName = "id", nullable = false)
    public User getCreation() {
        return creation;
    }

    public void setCreation(User userByCreation) {
        this.creation = userByCreation;
    }

    @OneToOne(mappedBy = "comment")
    public CommentHunter getCommentHunter() {
        return commentHunter;
    }

    public void setCommentHunter(CommentHunter commentHunterById) {
        this.commentHunter = commentHunterById;
    }

    @OneToOne(mappedBy = "comment")
    public CommentTask getCommentTask() {
        return commentTask;
    }

    public void setCommentTask(CommentTask commentTaskById) {
        this.commentTask = commentTaskById;
    }

    @OneToOne(mappedBy = "comment")
    public CommentUser getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(CommentUser commentUserById) {
        this.commentUser = commentUserById;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Comment comment = (Comment) o;
        return id.equals(comment.id) &&
                Double.compare(comment.number, number) == 0 &&
                creation.getId().equals(comment.getCreation().getId()) &&
                Objects.equals(type, comment.type) &&
                Objects.equals(context, comment.context) &&
                Objects.equals(createTime, comment.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, type, context, createTime, number, creation);
    }
}
