package com.tc.dto.task;

import com.tc.db.entity.*;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import com.tc.until.ListUtils;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 任务查询条件
 * @author Cyg
 */
public class QueryTask extends PageRequest {


    /**
     * 任务编号
     */
    private String id;
    /**
     * 用户编号
     */
    @NotNull
    @Min(value = 1)
    private Long userId;
    /**
     * 用户账户
     */
    private String account;
    /**
     * 用户名
     */
    private String username;
    private String taskName;
    private Float moneyBegin;
    private Float moneyEnd;
    /**
     * 任务状态（与任务状态组冲突）
     */
    private TaskState state;
    /**
     * 任务类别（与任务类别组冲突）
     */
    private TaskType type;
    /**
     * 任务状态组（与任务状态冲突）
     */
    private List<TaskState> states;
    /**
     * 任务类别组（与任务类别冲突）
     */
    private List<TaskType> types;
    /**
     * 任务可接人数（开始）（任务类别为多人时使用）（与任务类别组冲突）
     */
    private Integer peopleNumberBegin;
    /**
     * 任务可接人数（结束）（任务类别为多人时使用）（与任务类别组冲突）
     */
    private Integer peopleNumberEnd;
    private String context;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    private Timestamp beginTimeBegin;
    private Timestamp beginTimeEnd;
    private Timestamp deadlineBegin;
    private Timestamp deadlineEnd;
    private Integer permitAbandonMinuteBegin;
    private Integer permitAbandonMinuteEnd;
    private Boolean isTaskRework;
    private Boolean isCompensate;
    private Float commentAvgBegin;
    private Float commentAvgEnd;

    public QueryTask() {
        this(0,10);
    }

    public QueryTask(int page, int size) {
        super(page, size);
    }

    public QueryTask(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryTask(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTaskId() {
        return userId;
    }

    public void setTaskId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTaskname() {
        return username;
    }

    public void setTaskname(String username) {
        this.username = username;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Float getMoneyBegin() {
        return moneyBegin;
    }

    public void setMoneyBegin(Float moneyBegin) {
        this.moneyBegin = moneyBegin;
    }

    public Float getMoneyEnd() {
        return moneyEnd;
    }

    public void setMoneyEnd(Float moneyEnd) {
        this.moneyEnd = moneyEnd;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public List<TaskState> getStates() {
        return states;
    }

    public void setStates(List<TaskState> states) {
        this.states = states;
    }

    public List<TaskType> getTypes() {
        return types;
    }

    public void setTypes(List<TaskType> types) {
        this.types = types;
    }

    public Integer getPeopleNumberBegin() {
        return peopleNumberBegin;
    }

    public void setPeopleNumberBegin(Integer peopleNumberBegin) {
        this.peopleNumberBegin = peopleNumberBegin;
    }

    public Integer getPeopleNumberEnd() {
        return peopleNumberEnd;
    }

    public void setPeopleNumberEnd(Integer peopleNumberEnd) {
        this.peopleNumberEnd = peopleNumberEnd;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Timestamp getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Timestamp createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Timestamp getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Timestamp createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Timestamp getBeginTimeBegin() {
        return beginTimeBegin;
    }

    public void setBeginTimeBegin(Timestamp beginTimeBegin) {
        this.beginTimeBegin = beginTimeBegin;
    }

    public Timestamp getBeginTimeEnd() {
        return beginTimeEnd;
    }

    public void setBeginTimeEnd(Timestamp beginTimeEnd) {
        this.beginTimeEnd = beginTimeEnd;
    }

    public Timestamp getDeadlineBegin() {
        return deadlineBegin;
    }

    public void setDeadlineBegin(Timestamp deadlineBegin) {
        this.deadlineBegin = deadlineBegin;
    }

    public Timestamp getDeadlineEnd() {
        return deadlineEnd;
    }

    public void setDeadlineEnd(Timestamp deadlineEnd) {
        this.deadlineEnd = deadlineEnd;
    }

    public Integer getPermitAbandonMinuteBegin() {
        return permitAbandonMinuteBegin;
    }

    public void setPermitAbandonMinuteBegin(Integer permitAbandonMinuteBegin) {
        this.permitAbandonMinuteBegin = permitAbandonMinuteBegin;
    }

    public Integer getPermitAbandonMinuteEnd() {
        return permitAbandonMinuteEnd;
    }

    public void setPermitAbandonMinuteEnd(Integer permitAbandonMinuteEnd) {
        this.permitAbandonMinuteEnd = permitAbandonMinuteEnd;
    }

    public Boolean getTaskRework() {
        return isTaskRework;
    }

    public void setTaskRework(Boolean taskRework) {
        isTaskRework = taskRework;
    }

    public Boolean getCompensate() {
        return isCompensate;
    }

    public void setCompensate(Boolean compensate) {
        isCompensate = compensate;
    }

    public Float getCommentAvgBegin() {
        return commentAvgBegin;
    }

    public void setCommentAvgBegin(Float commentAvgBegin) {
        this.commentAvgBegin = commentAvgBegin;
    }

    public Float getCommentAvgEnd() {
        return commentAvgEnd;
    }

    public void setCommentAvgEnd(Float commentAvgEnd) {
        this.commentAvgEnd = commentAvgEnd;
    }


    public static List<Predicate> initPredicatesByTask(QueryTask queryTask, Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(QueryUtils.equals(root,cb,Task.ID,queryTask.id));
        predicates.add(QueryUtils.equals(root.get(Task.USER).get(User.ID),cb,queryTask.userId));
        predicates.add(QueryUtils.equals(root.get(Task.USER).get(User.USERNAME),cb,queryTask.getAccount()));
        predicates.add(QueryUtils.equals(root.get(Task.USER).get(User.NAME),cb,queryTask.getUsername()));
        predicates.add(QueryUtils.equals(root,cb,Task.IS_TASK_REWORK,queryTask.isTaskRework));
        predicates.add(QueryUtils.equals(root,cb,Task.IS_COMPENSATE,queryTask.isCompensate));

        predicates.add(QueryUtils.like(root,cb,Task.CONTEXT,queryTask.context));

        if (queryTask.state != null){
            if (queryTask.state.equals(TaskState.HUNTER_COMMIT)){
                Subquery<HunterTask> hunterTask = query.subquery(HunterTask.class);
                Root<HunterTask> xRoot = hunterTask.from(HunterTask.class);
                hunterTask.select(xRoot.get(HunterTask.TASK_ID));
                Predicate predicate = cb.equal(xRoot.get(HunterTask.HUNTER_TASK_STATE),HunterTaskState.COMMIT_TO_ADMIN);
                hunterTask.where(predicate);
                predicates.add(QueryUtils.in(root,cb,Task.ID,hunterTask));
            }else {
                predicates.add(QueryUtils.equals(root, cb, Task.TASK_STATE, queryTask.state));
            }
        }else {
            if (!ListUtils.isEmpty(queryTask.states)){
                predicates.add(QueryUtils.in(root,cb,Task.TASK_STATE,queryTask.states));
            }
        }

        if (queryTask.type != null){
            predicates.add(QueryUtils.equals(root,cb,Task.TASK_TYPE,queryTask.type));
            if (queryTask.type.equals(TaskType.MULTI)){
                predicates.add(QueryUtils.between(root,cb,Task.PEOPLE_NUMBER,queryTask.peopleNumberBegin,queryTask.peopleNumberEnd));
            }
        }else {
            if (!ListUtils.isEmpty(queryTask.types)){
                predicates.add(QueryUtils.in(root,cb,Task.TASK_TYPE,queryTask.types));
            }
        }

        predicates.add(QueryUtils.between(root,cb,Task.MONEY,queryTask.getMoneyBegin(),queryTask.getMoneyEnd()));
        predicates.add(QueryUtils.between(root,cb,Task.CREATE_TIME,queryTask.createTimeBegin,queryTask.createTimeEnd));
        predicates.add(QueryUtils.between(root,cb,Task.BEGIN_TIME,queryTask.beginTimeBegin,queryTask.beginTimeEnd));
        predicates.add(QueryUtils.between(root,cb,Task.DEADLINE,queryTask.deadlineBegin,queryTask.deadlineEnd));
        predicates.add(QueryUtils.between(root,cb,Task.PERMIT_ABANDON_MINUTE,queryTask.permitAbandonMinuteBegin,queryTask.permitAbandonMinuteEnd));

        if (!QueryUtils.hasNull(queryTask.getCommentAvgBegin()) || !QueryUtils.hasNull(queryTask.getCommentAvgEnd())){
            Subquery<Float> avg = QueryUtils.avgQuery(root,query,cb,CommentTask.class,CommentTask.COMMENT,Comment.NUMBER,CommentTask.TASK_ID,Task.ID);
            predicates.add(QueryUtils.between(cb,avg,queryTask.getCommentAvgBegin(),queryTask.getCommentAvgEnd()));
        }

        predicates.removeIf(Objects::isNull);

        return predicates;
    }
}
