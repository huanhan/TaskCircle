package com.tc.db.entity;

import com.tc.until.ListUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 评论猎刃
 * @author Cyg
 */
@Entity
@Table(name = "comment_hunter", schema = "tc-company")
public class CommentHunter {

    public static final String COMMENT_ID = "commentId";
    public static final String HUNTER_ID = "hunterId";
    public static final String HUNTER_TASK_ID = "hunterTaskId";
    public static final String COMMENT = "comment";
    public static final String HUNTER = "hunter";
    public static final String HUNTER_TASK = "hunterTask";

    private Long commentId;
    private Long hunterId;
    private String hunterTaskId;
    private Comment comment;
    private Hunter hunter;
    private HunterTask hunterTask;

    public CommentHunter() {
    }

    public CommentHunter(Hunter hunter, HunterTask hunterTask) {
        if (hunter != null){
            this.hunter = new Hunter(hunterId,hunter.getUser());
        }
        if (hunterTask != null){
            this.hunterTask = new HunterTask(hunterTaskId,hunterTask.getTask());
        }
    }


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

    public static List<CommentHunter> toListInIndex(List<CommentHunter> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(commentHunter -> {
                if (commentHunter.getComment() != null){
                    commentHunter.setComment(Comment.toDetail(commentHunter.comment));
                }
                if (commentHunter.getHunter() != null){
                    commentHunter.setHunter(new Hunter(commentHunter.getHunterId(),commentHunter.getHunter().getUser()));
                }
                if (commentHunter.getHunterTask() != null){
                    commentHunter.setHunterTask(new HunterTask(commentHunter.getHunterTaskId(),commentHunter.getHunterTask().getTask()));
                }
            });
        }
        return content;
    }

    public static CommentHunter toDetail(CommentHunter commentHunter) {
        if (commentHunter != null){
            if (commentHunter.getComment() != null){
                Comment comment = commentHunter.getComment();
                commentHunter.setComment(new Comment(
                        comment.getId(),
                        comment.getType(),
                        comment.getContext(),
                        comment.getCreateTime(),
                        comment.getNumber(),
                        comment.getCreationId(),
                        comment.getCreation()
                ));
            }
            if (commentHunter.getHunter() != null){
                commentHunter.setHunter(new Hunter(commentHunter.getHunterId(),commentHunter.getHunter().getUser()));
            }
            if (commentHunter.getHunterTask() != null){
                commentHunter.setHunterTask(new HunterTask(commentHunter.getHunterTaskId(),commentHunter.getHunterTask().getTask()));
            }
        }
        return commentHunter;
    }
}
