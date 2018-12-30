package com.tc.db.entity;

import com.tc.db.enums.HunterTaskState;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Cyg
 * 猎刃接取的任务
 */
@Entity
@Table(name = "hunter_task", schema = "tc-company")
public class HunterTask implements Serializable {
    private String id;
    private String taskId;
    private Long hunterId;
    private Task task;
    private Hunter hunter;
    private Timestamp acceptTime;
    private Timestamp finishTime;
    private String context;
    private Integer hunterRejectCount;
    private Integer userRejectCount;
    private HunterTaskState state;
    private Boolean isTaskRework;
    private Boolean isCompensate;
    private Collection<CommentHunter> commentHunters;
    private Collection<HunterTaskStep> hunterTaskSteps;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
    @Column(name = "accept_time")
    public Timestamp getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Timestamp acceptTime) {
        this.acceptTime = acceptTime;
    }

    @Basic
    @Column(name = "finish_time")
    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "hunter_reject_count")
    public Integer getHunterRejectCount() {
        return hunterRejectCount;
    }

    public void setHunterRejectCount(Integer hunterRejectCount) {
        this.hunterRejectCount = hunterRejectCount;
    }

    @Basic
    @Column(name = "user_reject_count")
    public Integer getUserRejectCount() {
        return userRejectCount;
    }

    public void setUserRejectCount(Integer userRegectCount) {
        this.userRejectCount = userRegectCount;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    public HunterTaskState getState() {
        return state;
    }

    public void setState(HunterTaskState state) {
        this.state = state;
    }

    @Basic
    @Column(name = "is_task_rework")
    public Boolean getTaskRework() {
        return isTaskRework;
    }

    public void setTaskRework(Boolean taskRework) {
        isTaskRework = taskRework;
    }

    @Basic
    @Column(name = "is_compensate")
    public Boolean getCompensate() {
        return isCompensate;
    }

    public void setCompensate(Boolean compensate) {
        isCompensate = compensate;
    }

    @OneToMany(mappedBy = "hunterTask")
    public Collection<CommentHunter> getCommentHunters() {
        return commentHunters;
    }

    public void setCommentHunters(Collection<CommentHunter> commentHuntersById) {
        this.commentHunters = commentHuntersById;
    }

    @OneToMany(mappedBy = "hunterTask")
    public Collection<HunterTaskStep> getHunterTaskSteps() {
        return hunterTaskSteps;
    }

    public void setHunterTaskSteps(Collection<HunterTaskStep> hunterTaskSteps) {
        this.hunterTaskSteps = hunterTaskSteps;
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
    @JoinColumn(name = "hunter_id", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunterByHunterId) {
        this.hunter = hunterByHunterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        HunterTask that = (HunterTask) o;
        return hunter.getUser().getId().equals(that.getHunter().getUser().getId()) &&
                isTaskRework.equals(that.getTaskRework()) &&
                isCompensate.equals(that.getCompensate()) &&
                Objects.equals(id, that.getId()) &&
                Objects.equals(task.getId(), that.getTask().getId()) &&
                Objects.equals(acceptTime, that.getAcceptTime()) &&
                Objects.equals(finishTime, that.getFinishTime()) &&
                Objects.equals(context, that.getContext()) &&
                Objects.equals(hunterRejectCount, that.getHunterRejectCount()) &&
                Objects.equals(userRejectCount, that.getUserRejectCount()) &&
                Objects.equals(state, that.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task.getId(), hunter.getUser().getId(), acceptTime, finishTime, context, hunterRejectCount, userRejectCount, state, isTaskRework, isCompensate);
    }
}
