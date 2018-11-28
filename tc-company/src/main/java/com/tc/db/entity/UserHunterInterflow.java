package com.tc.db.entity;

import com.tc.db.entity.pk.UserHunterInterflowPK;

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
    private Timestamp createTime;
    private String context;
    private Task task;
    private User user;
    private Hunter hunter;

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "hunter_id", referencedColumnName = "user_id", nullable = false)
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunterByHunterId) {
        this.hunter = hunterByHunterId;
    }

    @Id
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
                Objects.equals(task.getId(), that.getTask().getId()) &&
                Objects.equals(createTime, that.getCreateTime()) &&
                Objects.equals(context, that.getContext());
    }

    @Override
    public int hashCode() {

        return Objects.hash(task.getId(), user.getId(), hunter.getUser().getId(), createTime, context);
    }


}
