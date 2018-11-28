package com.tc.db.entity.pk;

import com.tc.db.entity.Hunter;
import com.tc.db.entity.Task;
import com.tc.db.entity.User;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class UserHunterInterflowPK implements Serializable {
    private Task task;
    private User user;
    private Hunter hunter;
    private Timestamp createTime;

    @Column(name = "task_id")
    @Id
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Column(name = "user_id")
    @Id
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "hunter_id")
    @Id
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    @Column(name = "create_time")
    @Id
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserHunterInterflowPK that = (UserHunterInterflowPK) o;
        return user.getId().equals(that.getUser().getId()) &&
                hunter.getUser().getId().equals(that.getHunter().getUser().getId()) &&
                Objects.equals(task.getId(), that.getTask().getId()) &&
                Objects.equals(createTime, that.getCreateTime());
    }

    @Override
    public int hashCode() {

        return Objects.hash(task.getId(), user.getId(), hunter.getUser().getId(), createTime);
    }
}
