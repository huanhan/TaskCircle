package com.tc.db.entity;

import com.tc.db.enums.AuditState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Cyg
 * 猎刃表
 */
@Entity
public class Hunter implements Serializable {
    private Long userId;
    private User user;
    private AuditState state;
    private Collection<CommentHunter> commentHunters;
    private Collection<HunterTask> hunterTasks;
    private Collection<UserHunterInterflow> userHunterInterflows;

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    public AuditState getState() {
        return state;
    }

    public void setState(AuditState state) {
        this.state = state;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hunter hunter = (Hunter) o;
        return user.getId().equals(hunter.getUser().getId()) &&
                Objects.equals(state, hunter.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), state);
    }
}
