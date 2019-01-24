package com.tc.dto.task;

import com.tc.db.entity.Hunter;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.User;
import com.tc.db.enums.HunterTaskState;
import com.tc.until.ListUtils;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 查询猎刃任务
 * @author Cyg
 */
public class QueryHunterTask extends PageRequest {

    private String id;
    private String taskId;
    private Long hunterId;
    private String hunterAccount;
    private Timestamp acceptTimeBegin;
    private Timestamp acceptTimeEnd;
    private Timestamp finishTimeBegin;
    private Timestamp finishTimeEnd;
    private Timestamp auditTimeBegin;
    private Timestamp auditTimeEnd;
    private Timestamp adminAuditTimeBegin;
    private Timestamp adminAuditTimeEnd;
    private Timestamp beginTimeBegin;
    private Timestamp beginTimeEnd;
    private String context;
    private String auditContext;
    private String hunterRejectContext;
    private HunterTaskState state;
    private List<HunterTaskState> states;
    private Boolean isStop;

    public QueryHunterTask(Long id, Timestamp acceptTimeBegin, Timestamp acceptTimeEnd) {
        super(0, 10);
        this.hunterId = id;
        this.acceptTimeBegin = acceptTimeBegin;
        this.acceptTimeEnd = acceptTimeEnd;
    }

    public QueryHunterTask() {
        super(0, 10);
    }

    public QueryHunterTask(int page, int size) {
        super(page, size);
    }

    public QueryHunterTask(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryHunterTask(int page, int size, Sort sort) {
        super(page, size, sort);
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getHunterAccount() {
        return hunterAccount;
    }

    public void setHunterAccount(String hunterAccount) {
        this.hunterAccount = hunterAccount;
    }

    public Timestamp getAcceptTimeBegin() {
        return acceptTimeBegin;
    }

    public void setAcceptTimeBegin(Timestamp acceptTimeBegin) {
        this.acceptTimeBegin = acceptTimeBegin;
    }

    public Timestamp getAcceptTimeEnd() {
        return acceptTimeEnd;
    }

    public void setAcceptTimeEnd(Timestamp acceptTimeEnd) {
        this.acceptTimeEnd = acceptTimeEnd;
    }

    public Timestamp getFinishTimeBegin() {
        return finishTimeBegin;
    }

    public void setFinishTimeBegin(Timestamp finishTimeBegin) {
        this.finishTimeBegin = finishTimeBegin;
    }

    public Timestamp getFinishTimeEnd() {
        return finishTimeEnd;
    }

    public void setFinishTimeEnd(Timestamp finishTimeEnd) {
        this.finishTimeEnd = finishTimeEnd;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public HunterTaskState getState() {
        return state;
    }

    public void setState(HunterTaskState state) {
        this.state = state;
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public Timestamp getAuditTimeBegin() {
        return auditTimeBegin;
    }

    public void setAuditTimeBegin(Timestamp auditTimeBegin) {
        this.auditTimeBegin = auditTimeBegin;
    }

    public Timestamp getAuditTimeEnd() {
        return auditTimeEnd;
    }

    public void setAuditTimeEnd(Timestamp auditTimeEnd) {
        this.auditTimeEnd = auditTimeEnd;
    }

    public Timestamp getAdminAuditTimeBegin() {
        return adminAuditTimeBegin;
    }

    public void setAdminAuditTimeBegin(Timestamp adminAuditTimeBegin) {
        this.adminAuditTimeBegin = adminAuditTimeBegin;
    }

    public Timestamp getAdminAuditTimeEnd() {
        return adminAuditTimeEnd;
    }

    public void setAdminAuditTimeEnd(Timestamp adminAuditTimeEnd) {
        this.adminAuditTimeEnd = adminAuditTimeEnd;
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

    public String getAuditContext() {
        return auditContext;
    }

    public void setAuditContext(String auditContext) {
        this.auditContext = auditContext;
    }

    public String getHunterRejectContext() {
        return hunterRejectContext;
    }

    public void setHunterRejectContext(String hunterRejectContext) {
        this.hunterRejectContext = hunterRejectContext;
    }

    public List<HunterTaskState> getStates() {
        return states;
    }

    public void setStates(List<HunterTaskState> states) {
        this.states = states;
    }

    public Boolean getStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }

    public static List<Predicate> initPredicatesByHunterTask(QueryHunterTask queryHunterTask, Root<HunterTask> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.equals(root,cb,HunterTask.ID,queryHunterTask.id));
        predicates.add(QueryUtils.equals(root,cb,HunterTask.TASK_ID,queryHunterTask.taskId));
        predicates.add(QueryUtils.equals(root,cb,HunterTask.HUNTER_ID,queryHunterTask.hunterId));
        predicates.add(QueryUtils.equals(root,cb,HunterTask.IS_STOP,queryHunterTask.isStop));
        predicates.add(QueryUtils.equals(root.get(HunterTask.HUNTER).get(Hunter.USER).get(User.USERNAME),cb,queryHunterTask.hunterAccount));
        predicates.add(QueryUtils.between(root,cb,HunterTask.ACCEPT_TIME,queryHunterTask.acceptTimeBegin,queryHunterTask.acceptTimeEnd));
        predicates.add(QueryUtils.between(root,cb,HunterTask.FINISH_TIME,queryHunterTask.finishTimeBegin,queryHunterTask.finishTimeEnd));
        predicates.add(QueryUtils.between(root,cb,HunterTask.ADMIN_AUDIT_TIME,queryHunterTask.adminAuditTimeBegin,queryHunterTask.adminAuditTimeEnd));
        predicates.add(QueryUtils.between(root,cb,HunterTask.AUDIT_TIME,queryHunterTask.auditTimeBegin,queryHunterTask.auditTimeEnd));
        predicates.add(QueryUtils.between(root,cb,HunterTask.BEGIN_TIME,queryHunterTask.beginTimeBegin,queryHunterTask.beginTimeEnd));
        predicates.add(QueryUtils.like(root,cb,HunterTask.CONTEXT,queryHunterTask.context));
        predicates.add(QueryUtils.like(root,cb,HunterTask.AUDIT_CONTEXT,queryHunterTask.auditContext));
        predicates.add(QueryUtils.like(root,cb,HunterTask.HUNTER_REJECT_CONTEXT,queryHunterTask.hunterRejectContext));

        if (ListUtils.isNotEmpty(queryHunterTask.states)){
            predicates.add(QueryUtils.in(root,cb,HunterTask.HUNTER_TASK_STATE,queryHunterTask.states));
        }else {
            predicates.add(QueryUtils.equals(root,cb,HunterTask.HUNTER_TASK_STATE,queryHunterTask.state));
        }

        predicates.removeIf(Objects::isNull);

        return predicates;
    }

    public static QueryHunterTask init(Long id, Timestamp begin, Timestamp end) {
        return new QueryHunterTask(id,begin,end);
    }
}
