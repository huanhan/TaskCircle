package com.tc.db.entity;

import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.MoneyType;
import com.tc.dto.task.step.HTStep;
import com.tc.dto.task.step.TransHT;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransData;
import com.tc.until.IdGenerator;
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
 * 猎刃接取的任务
 */
@Entity
@Table(name = "hunter_task", schema = "tc-company")
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
    public static final String IS_STOP = "stop";
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
    private HunterTaskState oldState;
    private Boolean isStop;
    private Float money;
    private MoneyType moneyType;
    private Long adminId;
    private Admin admin;

    private Collection<CommentHunter> commentHunters;
    private Collection<HunterTaskStep> hunterTaskSteps;
    private Collection<AuditHunterTask> auditHunterTasksById;
    private Collection<UserHunterInterflow> userHunterInterflows;
    private Boolean userCHunter;
    private Boolean hunterCUser;
    private Boolean hunterCTask;

    private Trans transState;
    private Trans transOState;
    private Trans transMoneyType;
    private Boolean isAudit = false;

    public HunterTask() {
    }

    public HunterTask(String id, Task task) {
        this.id = id;
        if (task != null) {
            this.task = new Task(task.getId(), task.getName());
        }
    }

    public HunterTask(String id, Hunter hunter) {
        this.id = id;
        if (hunter != null){
            this.hunter = new Hunter(hunter.getUserId(),hunter.getUser());
        }
    }

    public HunterTask(String id, String taskId, Hunter hunter) {
        this.id = id;
        this.taskId = taskId;
        if (hunter != null){
            this.hunter = new Hunter(hunter.getUserId(),hunter.getUser());
        }
    }
    public static List<HunterTask> toIndexAsList(List<HunterTask> content, Long me) {
        if (!ListUtils.isEmpty(content)) {
            content.forEach(hunterTask -> {
                if (hunterTask.getTask() != null) {
                    hunterTask.setTask(Task.toHunterDetail(hunterTask.getTask()));
                }
                if (hunterTask.getHunter() != null) {
                    hunterTask.getHunter().toDetail();
                }
                if (hunterTask.getAdmin() != null){
                    if (me != null){
                        if (hunterTask.getAdminId().equals(me)){
                            hunterTask.setAudit(true);
                        }
                    }
                    hunterTask.setAdmin(new Admin(hunterTask.adminId,hunterTask.admin.getUser()));
                }
                hunterTask.commentHunters = null;
                hunterTask.hunterTaskSteps = null;
                hunterTask.auditHunterTasksById = null;
                hunterTask.userHunterInterflows = null;
                hunterTask.setTransState(new Trans(hunterTask.state.name(),hunterTask.state.getState()));
                if (hunterTask.oldState != null){
                    hunterTask.setTransOState(new Trans(hunterTask.oldState.name(),hunterTask.oldState.getState()));
                }else {
                    hunterTask.setTransOState(new Trans());
                }
                if (hunterTask.moneyType != null){
                    hunterTask.setTransMoneyType(new Trans(hunterTask.moneyType.name(),hunterTask.moneyType.getType()));
                }else {
                    hunterTask.setTransMoneyType(new Trans());
                }
            });
        }
        return content;
    }
    public static List<HunterTask> toIndexAsList(List<HunterTask> content) {
        return toIndexAsList(content,null);
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
                hunterTask.task = Task.toHunterDetail(hunterTask.getTask());
            }
            if (hunterTask.hunter != null) {
                hunterTask.hunter = new Hunter(hunterTask.getHunterId(),hunterTask.getHunter().getUser());
            }
            if (hunterTask.getAdmin() != null){
                hunterTask.setAdmin(new Admin(hunterTask.adminId,hunterTask.admin.getUser()));
            }
            hunterTask.commentHunters = null;
            hunterTask.hunterTaskSteps = null;
            hunterTask.auditHunterTasksById = null;
            hunterTask.setTransState(new Trans(hunterTask.state.name(),hunterTask.state.getState()));
            if (hunterTask.oldState != null){
                hunterTask.setTransOState(new Trans(hunterTask.oldState.name(),hunterTask.oldState.getState()));
            }else {
                hunterTask.setTransOState(new Trans());
            }
            if (hunterTask.moneyType != null){
                hunterTask.setTransMoneyType(new Trans(hunterTask.moneyType.name(),hunterTask.moneyType.getType()));
            }else {
                hunterTask.setTransMoneyType(new Trans());
            }
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
        hunterTask.setUserCHunter(false);
        hunterTask.setHunterCUser(false);
        hunterTask.setHunterCTask(false);
        hunterTask.setHunterRejectCount(0);
        hunterTask.setUserRejectCount(0);
        hunterTask.setOldState(HunterTaskState.NONE);
        return hunterTask;
    }

    public static List<TransHT> toHTStepAsList(List<HunterTask> query,Integer count) {
        List<TransHT> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(query)){
            query.forEach(hunterTask -> {
                User user = hunterTask.getHunter().getUser();
                TransHT transHT = new TransHT();
                transHT.setUserId(user.getId());
                transHT.setName(user.getName());
                transHT.setUsername(user.getUsername());
                transHT.setHtId(hunterTask.getId());
                List<TransData> transData = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                    transData.add(new TransData("NO","未开始",i));
                }
                if (ListUtils.isNotEmpty(hunterTask.getHunterTaskSteps())){
                    Collection<HunterTaskStep> hunterTaskSteps = hunterTask.getHunterTaskSteps();
                    hunterTaskSteps.forEach(hunterTaskStep -> transData.forEach(trans -> {
                        if (trans.getData().equals(hunterTaskStep.getStep())){
                            trans.setValue("OK");
                            trans.setValue("已完成");
                        }
                    }));
                }
                transHT.setSteps(transData);
                result.add(transHT);
            });
        }
        return result;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "old_state")
    public HunterTaskState getOldState() {
        return oldState;
    }

    public void setOldState(HunterTaskState oldState) {
        this.oldState = oldState;
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

    @OneToMany(mappedBy = "hunterTask")
    public Collection<UserHunterInterflow> getUserHunterInterflows() {
        return userHunterInterflows;
    }

    public void setUserHunterInterflows(Collection<UserHunterInterflow> userHunterInterflows) {
        this.userHunterInterflows = userHunterInterflows;
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

    @Transient
    public Trans getTransState() {
        return transState;
    }

    public void setTransState(Trans transState) {
        this.transState = transState;
    }

    @Transient
    public Trans getTransOState() {
        return transOState;
    }

    public void setTransOState(Trans transOState) {
        this.transOState = transOState;
    }

    @Transient
    public Trans getTransMoneyType() {
        return transMoneyType;
    }

    public void setTransMoneyType(Trans transMoneyType) {
        this.transMoneyType = transMoneyType;
    }

    @Transient
    public Boolean getAudit() {
        return isAudit;
    }

    public void setAudit(Boolean audit) {
        isAudit = audit;
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
        if (admin != null){
            admin = new Admin(admin.getUserId(),admin.getUser());
        }
        commentHunters = null;
        hunterTaskSteps = null;
    }

    @Basic
    @Column(name = "admin_id")
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id",insertable = false,updatable = false)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
