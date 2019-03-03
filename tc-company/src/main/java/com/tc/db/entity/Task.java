package com.tc.db.entity;

import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransTaskBasic;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 用户发布的任务
 */
@Entity
public class Task implements Serializable {

    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String NAME = "name";
    public static final String MONEY = "money";
    public static final String ORIGINALMONEY = "originalMoney";
    public static final String TASK_STATE = "state";
    public static final String TASK_TYPE = "type";
    public static final String PEOPLE_NUMBER = "peopleNumber";
    public static final String CONTEXT = "context";
    public static final String CREATE_TIME = "createTime";
    public static final String AUDIT_TIME = "auditTime";
    public static final String BEGIN_TIME = "beginTime";
    public static final String DEADLINE = "deadline";
    public static final String ADMIN_AUDIT_TIME = "adminAuditTime";
    public static final String ISSUE_TIME = "issueTime";
    public static final String PERMIT_ABANDON_MINUTE = "permitAbandonMinute";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String IS_TASK_REWORK = "taskRework";
    public static final String IS_COMPENSATE = "compensate";


    public static final String USER = "user";

    public static final String TASK_CLASSIFY_RELATIONS= "taskClassifyRelations";

    private String id;
    private Long userId;
    private User user;
    private Long adminId;
    private Admin admin;
    private String name;
    private Float money;
    private Float originalMoney;
    private Float compensateMoney;
    private TaskState state;
    private TaskType type;
    private Integer peopleNumber;
    private String context;
    private Timestamp createTime;
    private Timestamp beginTime;
    private Timestamp deadline;
    private Timestamp auditTime;
    private Timestamp adminAuditTime;
    private Timestamp issueTime;
    private Integer permitAbandonMinute;
    private Double longitude;
    private Double latitude;
    private String address;
    private Boolean isTaskRework;
    private Boolean isCompensate;
    private Collection<AuditTask> auditTasks;
    private Collection<CommentTask> commentTasks;
    private Collection<CommentUser> commentUsers;
    private Collection<HunterTask> hunterTasks;
    private Collection<TaskClassifyRelation> taskClassifyRelations;
    private Collection<TaskStep> taskSteps;

    /**
     * 猎刃已经完成的数量
     */
    private Integer okCount = 0;
    /**
     * 正在执行的猎刃数量
     */
    private Integer currentCount = 0;
    /**
     * 未完成任务的猎刃的总押金
     */
    private Float cashCount = 0f;

    private Trans transType;
    private Trans transState;
    private List<Trans> classifies;
    /**
     * 是否允许被审核（只有在审核中的状态才会用到）
     */
    private Boolean isAudit = false;

    public Task() {
    }




    public Task(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Task(String id, String name, User user) {
        this.id = id;
        this.name = name;
        if (user != null){
            this.user = new User(user.getId(),user.getName(),user.getUsername());
        }
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
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
    @Column(name = "original_money")
    public Float getOriginalMoney() {
        return originalMoney;
    }

    public void setOriginalMoney(Float originalMoney) {
        this.originalMoney = originalMoney;
    }

    @Basic
    @Column(name = "compensate_money")
    public Float getCompensateMoney() {
        return compensateMoney;
    }

    public void setCompensateMoney(Float compensateMoney) {
        this.compensateMoney = compensateMoney;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    @Basic
    @Enumerated(EnumType.STRING)
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
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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
    @Column(name = "audit_time")
    public Timestamp getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime) {
        this.auditTime = auditTime;
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
    @Column(name = "admin_audit_time")
    public Timestamp getAdminAuditTime() {
        return adminAuditTime;
    }

    public void setAdminAuditTime(Timestamp adminAuditTime) {
        this.adminAuditTime = adminAuditTime;
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

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "is_rework")
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

    @Transient
    public Integer getOkCount() {
        return okCount;
    }

    public void setOkCount(Integer okCount) {
        this.okCount = okCount;
    }

    @Transient
    public Float getCashCount() {
        return cashCount;
    }

    public void setCashCount(Float cashCount) {
        this.cashCount = cashCount;
    }

    @Transient
    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    @Transient
    public Trans getTransType() {
        return transType;
    }

    public void setTransType(Trans transType) {
        this.transType = transType;
    }

    @Transient
    public Trans getTransState() {
        return transState;
    }

    public void setTransState(Trans transState) {
        this.transState = transState;
    }

    @Transient
    public List<Trans> getClassifies() {
        return classifies;
    }

    public void setClassifies(List<Trans> classifies) {
        this.classifies = classifies;
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
                Objects.equals(beginTime, task.getBeginTime()) &&
                Objects.equals(deadline, task.getDeadline()) &&
                Objects.equals(permitAbandonMinute, task.getPermitAbandonMinute()) &&
                Objects.equals(longitude, task.getLongitude()) &&
                Objects.equals(latitude, task.getLatitude()) &&
                isTaskRework.equals(task.getTaskRework()) &&
                isCompensate.equals(task.getCompensate()) ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user.getId(), name, money, state, type, peopleNumber, context, createTime, isTaskRework, isCompensate, beginTime, deadline, permitAbandonMinute, longitude, latitude);
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
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @OneToMany(mappedBy = "task",cascade = {CascadeType.MERGE})
    public Collection<TaskClassifyRelation> getTaskClassifyRelations() {
        return taskClassifyRelations;
    }

    public void setTaskClassifyRelations(Collection<TaskClassifyRelation> taskClassifyRelationsById) {
        this.taskClassifyRelations = taskClassifyRelationsById;
    }

    @OneToMany(mappedBy = "task",cascade = {CascadeType.MERGE})
    public Collection<TaskStep> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(Collection<TaskStep> taskStepsById) {
        this.taskSteps = taskStepsById;
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


    public void toDetail(){
        if (user != null){
            user = new User(user.getId(),user.getName(),user.getUsername());
        }
        auditTasks = null;
        commentTasks = null;
        if (taskClassifyRelations != null){
            taskClassifyRelations.forEach(tcr -> {
                tcr.setTask(null);
                if (tcr.getTaskClassify() != null) {
                    tcr.setTaskClassify(new TaskClassify(tcr.getTaskClassify().getId(), tcr.getTaskClassify().getName()));
                }
            });
        }
        taskSteps = null;
    }



    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    public static Task toDetail(Task task){
        if (task != null){
            if (task.getUser() != null){
                task.setUser(new User(task.getUser().getId(),task.getUser().getName(),task.getUser().getUsername()));
            }
            task.setAuditTasks(null);
            task.setCommentTasks(null);
            task.setCommentUsers(null);
            setHunterTask(task);
            task.classifies = new ArrayList<>();
            if (ListUtils.isNotEmpty(task.getTaskClassifyRelations())){
                task.getTaskClassifyRelations().forEach(tcr ->{
                    tcr.setTask(null);
                    if (tcr.getTaskClassify() != null) {
                        task.classifies.add(new Trans(tcr.getTaskClassify().getId(),tcr.getTaskClassify().getName()));
                    }
                });
                task.setTaskClassifyRelations(null);
            }
            if (task.getAdmin() != null){
                task.setAdmin(new Admin(task.getAdminId(),task.getAdmin().getUser()));
            }
            task.setTaskSteps(null);
            task.transState = new Trans(task.getState().name(),task.getState().getState());
            task.transType = new Trans(task.getType().name(),task.getType().getType());
        }
        return task;
    }

    public static Task toHunterDetail(Task task){
        if (task != null) {
            Task result = new Task(task.getId(), task.getName(), task.getUser());
            result.setCompensateMoney(task.compensateMoney);
            result.setOriginalMoney(task.originalMoney);
            result.setPeopleNumber(task.peopleNumber);
            return result;
        }
        return null;
    }

    public static List<Task> toIndexAsList(List<Task> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(task -> {
                if (task.getUser() != null){
                    task.setUser(new User(task.getUser().getId(),
                            task.getUser().getName(),
                            task.getUser().getUsername(),
                            task.getUser().getHeadImg(),
                            task.getUser().getCategory()));
                }
                if (task.getAdmin() != null){
                    task.setAdmin(new Admin(task.getAdminId(),task.getAdmin().getUser()));
                }
                setHunterTask(task);
                task.setTaskSteps(null);
                task.setTaskClassifyRelations(null);
                task.setCommentUsers(null);
                task.setCommentTasks(null);
                task.setAuditTasks(null);
                task.transState = new Trans(task.getState().name(),task.getState().getState());
                task.transType = new Trans(task.getType().name(),task.getType().getType());
            });
        }
        return content;
    }


    public static List<Task> toIndexAsList(List<Task> content,Long me) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(task -> {
                if (task.getUser() != null){
                    task.setUser(new User(task.getUser().getId(),
                            task.getUser().getName(),
                            task.getUser().getUsername(),
                            task.getUser().getHeadImg(),
                            task.getUser().getCategory()));
                }
                if (task.getAdmin() != null){
                    if (me.equals(task.getAdminId())){
                        task.isAudit = true;
                    }
                    task.setAdmin(new Admin(task.getAdminId(),task.getAdmin().getUser()));
                }
                setHunterTask(task);
                task.setTaskSteps(null);
                task.setTaskClassifyRelations(null);
                task.setCommentUsers(null);
                task.setCommentTasks(null);
                task.setAuditTasks(null);
                task.transState = new Trans(task.getState().name(),task.getState().getState());
                task.transType = new Trans(task.getType().name(),task.getType().getType());
            });
        }
        return content;
    }

    public static List<TransTaskBasic> toClassifyAsList(List<Task> content) {
        List<TransTaskBasic> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(content)){
            content.forEach(task -> {
                result.add(new TransTaskBasic(
                        new Trans(task.getState().name(),task.getState().getState()),
                        task.getId(),
                        task.getName(),
                        new Trans(task.getUser().getId(),task.getUser().toTitle())
                ));
            });
        }
        return result;
    }

    public static List<String> toIds(List<Task> tasks) {
        List<String> result = new ArrayList<>();
        if (!ListUtils.isEmpty(tasks)){
            tasks.forEach(task -> result.add(task.id));
        }
        return result;
    }

    private static void setHunterTask(Task task){
        if (ListUtils.isNotEmpty(task.hunterTasks)){
            task.getHunterTasks().forEach(hunterTask -> {
                if (HunterTaskState.isOk(hunterTask.getState())){
                    task.setOkCount(task.getOkCount() + 1);
                }else{
                    task.setCurrentCount(task.getCurrentCount() + 1);
                    task.setCashCount(FloatHelper.add(task.getCashCount(),task.getCompensateMoney()));
                }
            });
            task.setHunterTasks(null);
        }
    }
}
