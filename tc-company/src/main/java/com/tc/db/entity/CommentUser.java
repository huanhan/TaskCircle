package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 评论用户
 */
@Entity
@Table(name = "comment_user", schema = "tc-company")
public class CommentUser implements Serializable {

    private Comment comment;
    private User user;
    private Task task;

    @Id
    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment commentByCommentId) {
        this.comment = commentByCommentId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentUser that = (CommentUser) o;
        return comment.getId().equals(that.getComment().getId()) &&
                user.getId().equals(that.getUser().getId()) &&
                Objects.equals(task.getId(),that.getTask().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment.getId(), user.getId(), task.getId());
    }
}
