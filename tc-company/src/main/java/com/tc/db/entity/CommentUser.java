package com.tc.db.entity;

import com.tc.until.ListUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 评论用户
 * @author Cyg
 */
@Entity
@Table(name = "comment_user", schema = "tc-company")
public class CommentUser {

    public static final String COMMENT_ID = "commentId";
    public static final String USER_ID = "userId";
    public static final String TASK_ID = "taskId";
    public static final String COMMENT = "comment";
    public static final String USER = "user";
    public static final String TASK = "task";

    private Long commentId;
    private Long userId;
    private String taskId;
    private Comment comment;
    private User user;
    private Task task;

    public CommentUser() {
    }

    public CommentUser(User user, Task task) {
        if (user != null){
            this.userId = user.getId();
            this.user = new User(user.getId(),user.getName(),user.getUsername());
        }
        if (task != null){
            this.taskId = task.getId();
            this.task = new Task(task.getId(),task.getName());
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
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        CommentUser that = (CommentUser) o;
        return commentId.equals(that.commentId) &&
                userId.equals(that.userId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commentId, userId, taskId);
    }

    @OneToOne(mappedBy = "commentUser")
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public static List<CommentUser> toListInIndex(List<CommentUser> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(commentUser -> {
                if (commentUser.getComment() != null){
                    commentUser.setComment(Comment.toDetail(commentUser.getComment()));
                }
                if (commentUser.getUser() != null){
                    commentUser.setUser(new User(commentUser.getUserId(),commentUser.getUser().getName(),commentUser.getUser().getUsername()));
                }
                if (commentUser.getTask() != null){
                    commentUser.setTask(new Task(commentUser.getTaskId(),commentUser.getTask().getName()));
                }
            });
        }
        return content;
    }

    public static CommentUser toDetail(CommentUser commentUser) {
        if (commentUser != null){
            if (commentUser.getComment() != null){
                Comment comment = commentUser.getComment();
                commentUser.setComment(new Comment(
                        comment.getId(),
                        comment.getType(),
                        comment.getContext(),
                        comment.getCreateTime(),
                        comment.getNumber(),
                        comment.getCreationId(),
                        comment.getCreation()
                ));
            }
            if (commentUser.getUser() != null){
                commentUser.setUser(new User(commentUser.getUserId(),commentUser.getUser().getName(),commentUser.getUser().getUsername()));
            }
            if (commentUser.getTask() != null){
                commentUser.setTask(new Task(commentUser.getTaskId(),commentUser.getTask().getName()));
            }
        }
        return commentUser;
    }


}
