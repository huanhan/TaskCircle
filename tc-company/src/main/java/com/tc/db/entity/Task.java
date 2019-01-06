package com.tc.db.entity;

import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Cyg
 * 用户发布的任务
 */
@Entity
public class Task implements Serializable {

    public static final String MONEY = "money";


    private String id;
    private User user;
    private String name;
    private Float money;
    private TaskState state;
    private TaskType type;
    private Integer peopleNumber;
    private String context;
    private Timestamp createTime;
    private Timestamp issueTime;
    private Boolean isDelay;
    private Timestamp beginTime;
    private Timestamp deadline;
    private Integer permitAbandonMinute;
    private Double longitude;
    private Double latitude;
    private Collection<AuditTask> auditTasks;
    private Collection<CommentTask> commentTasks;
    private Collection<CommentUser> commentUsers;
    private Collection<HunterTask> hunterTasks;
    private Collection<TaskClassifyRelation> taskClassifyRelations;
    private Collection<TaskStep> taskSteps;
    private Collection<UserHunterInterflow> userHunterInterflows;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "money")
    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Basic
    @Column(name = "state")
    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    @Basic
    @Column(name = "type")
    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Basic
    @Column(name = "people_number")
    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
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
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "issue_time")
    public Timestamp getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Timestamp issueTime) {
        this.issueTime = issueTime;
    }

    @Basic
    @Column(name = "is_delay")
    public Boolean getDelay() {
        return isDelay;
    }

    public void setDelay(Boolean delay) {
        isDelay = delay;
    }

    @Basic
    @Column(name = "begin_time")
    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    @Basic
    @Column(name = "deadline")
    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    @Basic
    @Column(name = "permit_abandon_minute")
    public Integer getPermitAbandonMinute() {
        return permitAbandonMinute;
    }

    public void setPermitAbandonMinute(Integer permitAbandonMinute) {
        this.permitAbandonMinute = permitAbandonMinute;
    }

    @Basic
    @Column(name = "longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Task task = (Task) o;
        return user.getId().equals(task.getUser().getId()) &&
                Float.compare(task.getMoney(), money) == 0 &&
                peopleNumber.equals(task.getPeopleNumber()) &&
                Objects.equals(id, task.getId()) &&
                Objects.equals(name, task.getName()) &&
                Objects.equals(state, task.getState()) &&
                Objects.equals(type, task.getType()) &&
                Objects.equals(context, task.getContext()) &&
                Objects.equals(createTime, task.getCreateTime()) &&
                Objects.equals(issueTime, task.getIssueTime()) &&
                Objects.equals(isDelay, task.getDelay()) &&
                Objects.equals(beginTime, task.getBeginTime()) &&
                Objects.equals(deadline, task.getDeadline()) &&
                Objects.equals(permitAbandonMinute, task.getPermitAbandonMinute()) &&
                Objects.equals(longitude, task.getLongitude()) &&
                Objects.equals(latitude, task.getLatitude());
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user.getId(), name, money, state, type, peopleNumber, context, createTime, issueTime, isDelay, beginTime, deadline, permitAbandonMinute, longitude, latitude);
    }

    @OneToMany(mappedBy = "task")
    public Collection<AuditTask> getAuditTasks() {
        return auditTasks;
    }

    public void setAuditTasks(Collection<AuditTask> auditTasksById) {
        this.auditTasks = auditTasksById;
    }

    @OneToMany(mappedBy = "task")
    public Collection<CommentTask> getCommentTasks() {
        return commentTasks;
    }

    public void setCommentTasks(Collection<CommentTask> commentTasksById) {
        this.commentTasks = commentTasksById;
    }

    @OneToMany(mappedBy = "task")
    public Collection<CommentUser> getCommentUsers() {
        return commentUsers;
    }

    public void setCommentUsers(Collection<CommentUser> commentUsersById) {
        this.commentUsers = commentUsersById;
    }

    @OneToMany(mappedBy = "task")
    public Collection<HunterTask> getHunterTasks() {
        return hunterTasks;
    }

    public void setHunterTasks(Collection<HunterTask> hunterTasksById) {
        this.hunterTasks = hunterTasksById;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @OneToMany(mappedBy = "task")
    public Collection<TaskClassifyRelation> getTaskClassifyRelations() {
        return taskClassifyRelations;
    }

    public void setTaskClassifyRelations(Collection<TaskClassifyRelation> taskClassifyRelationsById) {
        this.taskClassifyRelations = taskClassifyRelationsById;
    }

    @OneToMany(mappedBy = "task")
    public Collection<TaskStep> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(Collection<TaskStep> taskStepsById) {
        this.taskSteps = taskStepsById;
    }

    @OneToMany(mappedBy = "task")
    public Collection<UserHunterInterflow> getUserHunterInterflows() {
        return userHunterInterflows;
    }

    public void setUserHunterInterflows(Collection<UserHunterInterflow> userHunterInterflowsById) {
        this.userHunterInterflows = userHunterInterflowsById;
    }
}
