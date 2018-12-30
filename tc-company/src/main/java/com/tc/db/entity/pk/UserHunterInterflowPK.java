package com.tc.db.entity.pk;


import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * 用户猎刃针对任务交流的主键
 * @author Cyg
 */
public class UserHunterInterflowPK implements Serializable {
    private String taskId;
    private Long userId;
    private Long hunterId;
    private Timestamp createTime;

    @Column(name = "task_id")
    @Id
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "user_id")
    @Id
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "hunter_id")
    @Id
    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
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
        return userId.equals(that.getUserId()) &&
                hunterId.equals(that.getHunterId()) &&
                taskId.equals(that.getTaskId()) &&
                Objects.equals(createTime, that.getCreateTime());
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskId, userId, hunterId, createTime);
    }
}
