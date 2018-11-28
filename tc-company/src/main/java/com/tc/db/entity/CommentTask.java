package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 评论任务
 */
@Entity
@Table(name = "comment_task", schema = "tc-company")
public class CommentTask implements Serializable {
    private Comment comment;
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
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        CommentTask that = (CommentTask) o;
        return comment.getId().equals(that.getComment().getId()) &&
                Objects.equals(task.getId(), that.getTask().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment.getId(), task.getId());
    }
}
