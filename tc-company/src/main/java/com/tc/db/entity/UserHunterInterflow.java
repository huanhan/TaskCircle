package com.tc.db.entity;

import com.tc.db.entity.pk.UserHunterInterflowPK;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Cyg
 * 用户猎刃交流表
 */
@Entity
@Table(name = "user_hunter_interflow", schema = "tc-company")
@IdClass(UserHunterInterflowPK.class)
public class UserHunterInterflow implements Serializable {


    private Long hunterId;
    private Long userId;
    private String taskId;
    private Timestamp createTime;
    private String context;
    private Task task;
    private User user;
    private Hunter hunter;

    @Id
    @Column(name = "hunter_id")
    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_id", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunterByHunterId) {
        this.hunter = hunterByHunterId;
    }

    @Id
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserHunterInterflow that = (UserHunterInterflow) o;
        return user.getId().equals(that.getUser().getId()) &&
                hunter.getUser().getId().equals(that.getHunter().getUser().getId()) &&
                taskId.equals(that.getTaskId()) &&
                userId.equals(that.getUserId()) &&
                hunterId.equals(that.getHunterId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(task.getId(), user.getId(), hunter.getUser().getId(), createTime, context);
    }


}
