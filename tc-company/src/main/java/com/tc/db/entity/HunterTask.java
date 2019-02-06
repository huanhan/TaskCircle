package com.tc.db.entity;

import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.MoneyType;
import com.tc.until.IdGenerator;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 猎刃接取的任务
 */
@Entity
@Table(name = "hunter_task", schema = "tc-company", catalog = "")
public class HunterTask implements Serializable {

    public static final String ID = "id";
    public static final String TASK_ID = "taskId";
    public static final String HUNTER_ID = "hunterId";
    public static final String HUNTER_TASK_STATE = "state";
    public static final String ACCEPT_TIME = "acceptTime";
    public static final String AUDIT_TIME = "auditTime";
    public static final String FINISH_TIME = "finishTime";
    public static final String ADMIN_AUDIT_TIME = "adminAuditTime";
    public static final String BEGIN_TIME = "beginTime";
    public static final String CONTEXT = "context";
    public static final String AUDIT_CONTEXT = "auditContext";
    public static final String HUNTER_REJECT_CONTEXT = "hunterRejectContext";
    public static final String TASK = "task";
    public static final String HUNTER = "hunter";
    public static final String IS_STOP = "isStop";
    public static final String MONEY = "money";
    public static final String MONEY_TYPE = "moneyType";


    private String id;
    private String taskId;
    private Long hunterId;
    private Task task;
    private Hunter hunter;
    private Timestamp acceptTime;
    private Timestamp finishTime;
    private Timestamp auditTime;
    private Timestamp adminAuditTime;
    private Timestamp beginTime;
    private String context;
    private String auditContext;
    private String hunterRejectContext;
    private Integer hunterRejectCount;
    private Integer userRejectCount;
    private HunterTaskState state;
    private Boolean isStop;
    private Float money;
    private MoneyType moneyType;

    private Collection<CommentHunter> commentHunters;
    private Collection<HunterTaskStep> hunterTaskSteps;
    private Collection<AuditHunterTask> auditHunterTasksById;
    private Boolean userCHunter;
    private Boolean hunterCUser;
    private Boolean hunterCTask;

    public HunterTask() {
    }

    public HunterTask(String id, Task task) {
        this.id = id;
        if (task != null) {
            this.task = new Task(task.getId(), task.getName());
        }
    }

    public static List<HunterTask> toIndexAsList(List<HunterTask> content) {
        if (!ListUtils.isEmpty(content)) {
            content.forEach(hunterTask -> {
                if (hunterTask.getTask() != null) {
                    hunterTask.setTask(new Task(hunterTask.getTask().getId(), hunterTask.getTask().getName()));
                }
                if (hunterTask.getHunter() != null) {
                    hunterTask.getHunter().toDetail();
                }
                hunterTask.commentHunters = null;
                hunterTask.hunterTaskSteps = null;
                hunterTask.auditHunterTasksById = null;
            });
        }
        return content;
    }

    public static List<String> toIds(List<HunterTask> tasks) {
        List<String> result = new ArrayList<>();
        if (!ListUtils.isEmpty(tasks)) {
            tasks.forEach(task -> result.add(task.id));
        }
        return result;
    }

    public static List<Long> toUserIds(List<HunterTask> tasks) {
        List<Long> result = new ArrayList<>();
        if (!ListUtils.isEmpty(tasks)) {
            tasks.forEach(task -> result.add(task.hunterId));
        }
        return result;
    }

    public static HunterTask toDetail(HunterTask hunterTask) {
        if (hunterTask != null) {
            if (hunterTask.task != null) {
                hunterTask.task = new Task(hunterTask.taskId, hunterTask.task.getName());
            }
            if (hunterTask.hunter != null) {
                hunterTask.hunter.toDetail();
            }
            hunterTask.commentHunters = null;
            hunterTask.hunterTaskSteps = null;
            hunterTask.auditHunterTasksById = null;
        }
        return hunterTask;
    }

    /**
     * 新增一条猎刃任务记录
     *
     * @return
     */
    public static HunterTask init(String taskId, Long hunterId) {
        HunterTask hunterTask = new HunterTask();
        hunterTask.setId(IdGenerator.INSTANCE.nextId());
        hunterTask.setTaskId(taskId);
        hunterTask.setHunterId(hunterId);
        hunterTask.setStop(false);
        hunterTask.setState(HunterTaskState.RECEIVE);
        return hunterTask;
    }

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
    @CreationTimestamp
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
    @Column(name = "audit_time")
    public Timestamp getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime) {
        this.auditTime = auditTime;
    }

    @Basic
    @Column(name = "admin_audit_time")
    public Timestamp getAdminAuditTime() {
        return adminAuditTime;
    }

    public void setAdminAuditTime(Timestamp adminAuditTime) {
        this.adminAuditTime = adminAuditTime;
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
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "audit_context")
    public String getAuditContext() {
        return auditContext;
    }

    public void setAuditContext(String auditContext) {
        this.auditContext = auditContext;
    }

    @Basic
    @Column(name = "hunter_reject_context")
    public String getHunterRejectContext() {
        return hunterRejectContext;
    }

    public void setHunterRejectContext(String hunterRejectContext) {
        this.hunterRejectContext = hunterRejectContext;
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
    @Column(name = "is_stop")
    public Boolean getStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "money_type")
    public MoneyType getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(MoneyType moneyType) {
        this.moneyType = moneyType;
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
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunterByHunterId) {
        this.hunter = hunterByHunterId;
    }

    @OneToMany(mappedBy = "hunterTask")
    public Collection<AuditHunterTask> getAuditHunterTasksById() {
        return auditHunterTasksById;
    }

    public void setAuditHunterTasksById(Collection<AuditHunterTask> auditHunterTasksById) {
        this.auditHunterTasksById = auditHunterTasksById;
    }

    @Basic
    @Column(name = "user_c_hunter")
    public Boolean getUserCHunter() {
        return userCHunter;
    }

    public void setUserCHunter(Boolean userCHunter) {
        this.userCHunter = userCHunter;
    }

    @Basic
    @Column(name = "hunter_c_user")
    public Boolean getHunterCUser() {
        return hunterCUser;
    }

    public void setHunterCUser(Boolean hunterCUser) {
        this.hunterCUser = hunterCUser;
    }

    @Basic
    @Column(name = "hunter_c_task")
    public Boolean getHunterCTask() {
        return hunterCTask;
    }

    public void setHunterCTask(Boolean hunterCTask) {
        this.hunterCTask = hunterCTask;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HunterTask that = (HunterTask) o;
        return hunter.getUser().getId().equals(that.getHunter().getUser().getId()) &&
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
        return Objects.hash(id, task.getId(), hunter.getUser().getId(), acceptTime, finishTime, context, hunterRejectCount, userRejectCount, state);
    }

    public void toDetail() {
        if (task != null) {
            task = new Task(taskId, task.getName());
        }
        if (hunter != null) {
            hunter.toDetail();
        }
        commentHunters = null;
        hunterTaskSteps = null;
    }


}
