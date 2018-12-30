package com.tc.db.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * 评论猎刃
 * @author Cyg
 */
@Entity
@Table(name = "comment_hunter", schema = "tc-company")
public class CommentHunter {
    private Long commentId;
    private Long hunterId;
    private String hunterTaskId;
    private Comment comment;
    private Hunter hunter;
    private HunterTask hunterTask;

    @Id
    @Column(name = "comment_id")
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "hunter_id")
    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    @Basic
    @Column(name = "hunter_task_id")
    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentHunter that = (CommentHunter) o;
        return commentId.equals(that.commentId) &&
                hunterId.equals(that.hunterId) &&
                Objects.equals(hunterTaskId, that.hunterTaskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commentId, hunterId, hunterTaskId);
    }

    @OneToOne(mappedBy = "commentHunter")
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_id", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public HunterTask getHunterTask() {
        return hunterTask;
    }

    public void setHunterTask(HunterTask hunterTask) {
        this.hunterTask = hunterTask;
    }
}
