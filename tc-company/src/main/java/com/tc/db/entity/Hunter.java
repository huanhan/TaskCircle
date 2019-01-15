package com.tc.db.entity;


import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 猎刃表
 */
@Entity
public class Hunter implements Serializable {

    public static final String USER_ID = "userId";
    public static final String USER = "user";

    private Long userId;
    private User user;
    private Timestamp createTime;
    private Collection<CommentHunter> commentHunters;
    private Collection<HunterTask> hunterTasks;
    private Collection<UserHunterInterflow> userHunterInterflows;

    public Hunter() {
    }

    public Hunter(Long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    @OneToMany(mappedBy = "hunter")
    public Collection<CommentHunter> getCommentHunters() {
        return commentHunters;
    }

    public void setCommentHunters(Collection<CommentHunter> commentHuntersByUserId) {
        this.commentHunters = commentHuntersByUserId;
    }

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @OneToMany(mappedBy = "hunter")
    public Collection<HunterTask> getHunterTasks() {
        return hunterTasks;
    }

    public void setHunterTasks(Collection<HunterTask> hunterTasksByUserId) {
        this.hunterTasks = hunterTasksByUserId;
    }

    @OneToMany(mappedBy = "hunter")
    public Collection<UserHunterInterflow> getUserHunterInterflows() {
        return userHunterInterflows;
    }

    public void setUserHunterInterflows(Collection<UserHunterInterflow> userHunterInterflowsByUserId) {
        this.userHunterInterflows = userHunterInterflowsByUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hunter hunter = (Hunter) o;
        return user.getId().equals(hunter.getUser().getId()) &&
                Objects.equals(createTime, hunter.getCreateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), createTime);
    }

    public void toDetail(){
        if (user != null){
            user = new User(userId,user.getName(),user.getUsername());
        }
        commentHunters = null;
        hunterTasks = null;
        userHunterInterflows = null;
    }

    public static List<Long> toIds(List<Hunter> queryResult) {
        List<Long> result = new ArrayList<>();
        if (!ListUtils.isEmpty(queryResult)){
            queryResult.forEach(hunter -> result.add(hunter.getUserId()));
        }
        return result;
    }
}
