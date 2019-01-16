package com.tc.db.entity;

import com.tc.until.ListUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 评论任务
 * @author Cyg
 */
@Entity
@Table(name = "comment_task", schema = "tc-company")
public class CommentTask {

    public static final String TASK_ID = "taskId";
    public static final String COMMENT_ID = "commentId";
    public static final String COMMENT = "comment";

    private Long commentId;
    private String taskId;
    private Comment comment;
    private Task task;




    @Id
    @Column(name = "comment_id")
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentTask that = (CommentTask) o;
        return commentId.equals(that.commentId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commentId, taskId);
    }

    @OneToOne(mappedBy = "commentTask")
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public static List<CommentTask> toListInIndex(List<CommentTask> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(commentTask -> {
                if (commentTask.getComment() != null){
                    commentTask.setComment(new Comment(commentTask.getComment().getCreateTime(),commentTask.getComment().getCreation()));
                }
                if (commentTask.getTask() != null){
                    commentTask.setTask(new Task(commentTask.getTaskId(),commentTask.getTask().getName()));
                }
            });
        }
        return content;
    }

    public static CommentTask toDetail(CommentTask commentTask) {
        if (commentTask != null){
            if (commentTask.getComment() != null){
                Comment comment = commentTask.getComment();
                commentTask.setComment(new Comment(
                        comment.getId(),
                        comment.getType(),
                        comment.getContext(),
                        comment.getCreateTime(),
                        comment.getNumber(),
                        comment.getCreationId(),
                        comment.getCreation()
                ));
            }
            if (commentTask.getTask() != null){
                commentTask.setTask(new Task(commentTask.getTaskId(),commentTask.getTask().getName()));
            }
        }
        return commentTask;
    }
}
