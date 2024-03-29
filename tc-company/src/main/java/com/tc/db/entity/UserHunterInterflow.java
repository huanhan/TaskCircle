package com.tc.db.entity;

import com.tc.db.entity.pk.UserHunterInterflowPK;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 用户猎刃交流表
 */
@Entity
@Table(name = "user_hunter_interflow", schema = "tc-company")
@IdClass(UserHunterInterflowPK.class)
public class UserHunterInterflow implements Serializable {

    public static final String HUNTER_ID = "hunterId";
    public static final String USER_ID = "userId";
    public static final String HUNTER_TASK_ID = "hunterTaskId";
    public static final String SENDER = "sender";
    public static final String CREATE_TIME = "createTime";
    public static final String CONTEXT = "context";
    public static final String HUNTER_TASK = "hunterTask";
    public static final String USER = "user";
    public static final String HUNTER = "hunter";
    public static final String COUNT = "count";



    private Long hunterId;
    private Long userId;
    private Long sender;
    private String hunterTaskId;
    private Timestamp createTime;
    private String context;
    private HunterTask hunterTask;
    private User user;
    private Hunter hunter;
    private Long count;

    public static List<UserHunterInterflow> toListInIndex(List<UserHunterInterflow> content) {
        if (ListUtils.isNotEmpty(content)){
            content.forEach(userHunterInterflow -> {
                if (userHunterInterflow.hunter != null){
                    Hunter hunter = userHunterInterflow.hunter;
                    userHunterInterflow.hunter = new Hunter(hunter.getUserId(),hunter.getUser());
                }
                if (userHunterInterflow.user != null){
                    User user = userHunterInterflow.user;
                    userHunterInterflow.user = new User(userHunterInterflow.userId,user.getName(),user.getUsername(),user.getHeadImg());
                }
                if (userHunterInterflow.hunterTask != null){
                    HunterTask hunterTask = userHunterInterflow.hunterTask;
                    userHunterInterflow.hunterTask = new HunterTask(hunterTask.getId(),hunterTask.getTask());
                }
            });
        }
        return content;
    }

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

    @Column(name = "sender")
    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    @Id
    @Column(name = "hunter_task_id")
    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public HunterTask getHunterTask() {
        return hunterTask;
    }

    public void setHunterTask(HunterTask hunterTask) {
        this.hunterTask = hunterTask;
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

    @Transient
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserHunterInterflow that = (UserHunterInterflow) o;
        return user.getId().equals(that.getUser().getId()) &&
                hunter.getUser().getId().equals(that.getHunter().getUser().getId()) &&
                hunterTaskId.equals(that.getHunterTaskId()) &&
                userId.equals(that.getUserId()) &&
                sender.equals(that.getSender()) &&
                hunterId.equals(that.getHunterId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(hunterTask.getId(), user.getId(), hunter.getUser().getId(), createTime, context);
    }


}
