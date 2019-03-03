package com.tc.dto.task;

import com.tc.db.entity.*;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 查询任务聊天记录
 * @author Cyg
 */
public class QueryTaskInterflow extends PageRequest {

    @NotNull
    @Min(1)
    private Long hunterId;
    private String hunterAccount;
    private String hunterName;
    @NotNull
    @Min(1)
    private Long userId;
    private String userAccount;
    private String userName;
    @NotEmpty
    @Length(max = 32)
    private String htId;
    private String taskId;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    private String context;

    public QueryTaskInterflow() {
        super(0,10);
    }

    public QueryTaskInterflow(int page, int size) {
        super(page, size);
    }

    public QueryTaskInterflow(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryTaskInterflow(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public String getHunterAccount() {
        return hunterAccount;
    }

    public void setHunterAccount(String hunterAccount) {
        this.hunterAccount = hunterAccount;
    }

    public String getHunterName() {
        return hunterName;
    }

    public void setHunterName(String hunterName) {
        this.hunterName = hunterName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getHtId() {
        return htId;
    }

    public void setHtId(String htId) {
        this.htId = htId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public static List<Predicate> initPredicatesByTask(QueryTaskInterflow queryTaskInterflow, Root<UserHunterInterflow> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(QueryUtils.equals(root,cb,UserHunterInterflow.HUNTER_ID,queryTaskInterflow.hunterId));
        predicates.add(QueryUtils.equals(root.get(UserHunterInterflow.HUNTER).get(Hunter.USER).get(User.NAME),cb,queryTaskInterflow.hunterName));
        predicates.add(QueryUtils.equals(root.get(UserHunterInterflow.HUNTER).get(Hunter.USER).get(User.USERNAME),cb,queryTaskInterflow.hunterAccount));
        predicates.add(QueryUtils.equals(root,cb,UserHunterInterflow.USER_ID,queryTaskInterflow.userId));
        predicates.add(QueryUtils.equals(root.get(UserHunterInterflow.USER).get(User.NAME),cb,queryTaskInterflow.userName));
        predicates.add(QueryUtils.equals(root.get(UserHunterInterflow.USER).get(User.USERNAME),cb,queryTaskInterflow.userAccount));
        predicates.add(QueryUtils.equals(root.get(UserHunterInterflow.HUNTER_TASK).get(HunterTask.ID),cb,queryTaskInterflow.htId));
        predicates.add(QueryUtils.equals(root.get(UserHunterInterflow.HUNTER_TASK).get(HunterTask.TASK).get(Task.ID),cb,queryTaskInterflow.taskId));
        predicates.add(QueryUtils.between(root,cb,UserHunterInterflow.CREATE_TIME,queryTaskInterflow.createTimeBegin,queryTaskInterflow.createTimeEnd));
        predicates.add(QueryUtils.like(root,cb,UserHunterInterflow.CONTEXT,queryTaskInterflow.context));
        predicates.removeIf(Objects::isNull);
        return predicates;
    }
}
