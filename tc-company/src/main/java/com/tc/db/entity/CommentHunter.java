package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 评论猎刃
 */
@Entity
@Table(name = "comment_hunter", schema = "tc-company")
public class CommentHunter implements Serializable {

    private Comment comment;
    private Hunter hunter;
    private HunterTask hunterTask;

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
    @JoinColumn(name = "hunter_id", referencedColumnName = "user_id", nullable = false)
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunterByHunterId) {
        this.hunter = hunterByHunterId;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_task_id", referencedColumnName = "id", nullable = false)
    public HunterTask getHunterTask() {
        return hunterTask;
    }

    public void setHunterTask(HunterTask hunterTaskByHunterTaskId) {
        this.hunterTask = hunterTaskByHunterTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentHunter that = (CommentHunter) o;
        return comment.getId().equals(that.getComment().getId()) &&
                hunter.getUser().getId().equals(that.getHunter().getUser().getId()) &&
                Objects.equals(hunterTask.getId(), that.getHunterTask().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment.getId(), hunter.getUser().getId(), hunterTask.getId());
    }
}
