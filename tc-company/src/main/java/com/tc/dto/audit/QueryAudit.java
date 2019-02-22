package com.tc.dto.audit;

import com.tc.db.entity.*;
import com.tc.db.entity.Audit;
import com.tc.db.enums.AuditState;
import com.tc.db.enums.AuditType;
import com.tc.db.enums.UserCategory;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import com.tc.until.SpecificationFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 管理员审核查询条件
 * @author Cyg
 */
public class QueryAudit extends PageRequest {

    private Long adminId;
    private String adminName;
    private String adminAccount;
    private String idea;
    private AuditState result;
    private String reason;
    private AuditType type;
    private Timestamp createBegin;
    private Timestamp createEnd;
    private Float moneyBegin;
    private Float moneyEnd;
    private Float wdMoneyBegin;
    private Float wdMoneyEnd;
    private Long userId;
    private UserCategory category;


    public QueryAudit() {
        super(0, 10);
    }

    public QueryAudit(AuditType auditType){
        super(0, 10);
        this.type = auditType;
    }


    public QueryAudit(int page, int size) {
        super(page, size);
    }

    public QueryAudit(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryAudit(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(String adminAccount) {
        this.adminAccount = adminAccount;
    }

    public Long getAuditId() {
        return adminId;
    }

    public void setAuditId(Long adminId) {
        this.adminId = adminId;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public AuditState getResult() {
        return result;
    }

    public void setResult(AuditState result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AuditType getType() {
        return type;
    }

    public void setType(AuditType type) {
        this.type = type;
    }

    public Timestamp getCreateBegin() {
        return createBegin;
    }

    public void setCreateBegin(Timestamp createBegin) {
        this.createBegin = createBegin;
    }

    public Timestamp getCreateEnd() {
        return createEnd;
    }

    public void setCreateEnd(Timestamp createEnd) {
        this.createEnd = createEnd;
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

    public Float getWdMoneyBegin() {
        return wdMoneyBegin;
    }

    public void setWdMoneyBegin(Float wdMoneyBegin) {
        this.wdMoneyBegin = wdMoneyBegin;
    }

    public Float getWdMoneyEnd() {
        return wdMoneyEnd;
    }

    public void setWdMoneyEnd(Float wdMoneyEnd) {
        this.wdMoneyEnd = wdMoneyEnd;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public static List<Predicate> initPredicates(QueryAudit queryAudit, Root<Audit> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        List<Predicate> predicates = new ArrayList<>();
        if (queryAudit.getAdminId() != null && queryAudit.getAdminId() > 0){
            predicates.add(cb.equal(root.get(Audit.ADMIN_ID),queryAudit.getAdminId()));
        }
        if (!StringUtils.isEmpty(queryAudit.getAdminName())){
            predicates.add(cb.equal(root.get(Audit.ADMIN).get(Admin.USER).get(User.NAME),queryAudit.adminName));
        }
        if (!StringUtils.isEmpty(queryAudit.getAdminAccount())){
            predicates.add(cb.equal(root.get(Audit.ADMIN).get(Admin.USER).get(User.USERNAME),queryAudit.adminAccount));
        }
        if (queryAudit.getResult() != null){
            predicates.add(cb.equal(root.get(Audit.RESULT),queryAudit.getResult()));
        }
        if (!StringUtils.isEmpty(queryAudit.getIdea())){
            predicates.add(cb.like(root.get(Audit.IDEA),"%" + queryAudit.getIdea() + "%"));
        }
        if (!StringUtils.isEmpty(queryAudit.getReason())){
            predicates.add(cb.like(root.get(Audit.REASON),"%" + queryAudit.getReason() + "%"));
        }
        if (queryAudit.getType() != null){
            predicates.add(cb.equal(root.get(Audit.TYPE),queryAudit.getType()));
            switch (queryAudit.getType()){
                case HUNTER:
                    break;
                case WITHDRAW:
                    if (queryAudit.getWdMoneyBegin() != null || queryAudit.getWdMoneyEnd() != null){
                        if (queryAudit.getWdMoneyBegin() != null && queryAudit.getWdMoneyEnd() != null){
                            predicates.add(
                                    cb.between(
                                            root
                                                .get(Audit.AUDIT_WITHDRAW)
                                                .get(AuditWithdraw.USER_WITHDRAW)
                                                .get(UserWithdraw.MONEY),
                                            queryAudit.getWdMoneyBegin(),
                                            queryAudit.getWdMoneyEnd()
                                    )
                            );
                        }else if (queryAudit.getWdMoneyBegin() != null){
                            predicates.add(cb.greaterThan(root
                                    .get(Audit.AUDIT_WITHDRAW)
                                    .get(AuditWithdraw.USER_WITHDRAW)
                                    .get(UserWithdraw.MONEY),
                                    queryAudit.getWdMoneyBegin()
                                )
                            );
                        }else if (queryAudit.getWdMoneyEnd() != null){
                            predicates.add(cb.lessThan(root
                                    .get(Audit.AUDIT_WITHDRAW)
                                    .get(AuditWithdraw.USER_WITHDRAW)
                                    .get(UserWithdraw.MONEY),
                                    queryAudit.getWdMoneyEnd()
                                )
                            );
                        }
                    }
                    break;
                case TASK:
                    if (queryAudit.getMoneyBegin() != null || queryAudit.getMoneyEnd() != null){
                        if (queryAudit.getMoneyBegin() != null && queryAudit.getMoneyEnd() != null){
                            predicates.add(cb.between(
                                    root.get(Audit.AUDIT_TASK).get(AuditTask.TASK).get(Task.MONEY),
                                    queryAudit.getMoneyBegin(),queryAudit.getMoneyEnd()
                            ));
                        }else if (queryAudit.getMoneyBegin() != null){
                            predicates.add(cb.greaterThan(
                                    root.get(Audit.AUDIT_TASK).get(AuditTask.TASK).get(Task.MONEY),
                                    queryAudit.getMoneyBegin()
                            ));
                        }else if (queryAudit.getMoneyEnd() != null){
                            predicates.add(cb.lessThan(
                                    root.get(Audit.AUDIT_TASK).get(AuditTask.TASK).get(Task.MONEY),
                                    queryAudit.getMoneyEnd()
                            ));
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if (queryAudit.getCreateBegin() != null || queryAudit.getCreateEnd() != null){
            if (queryAudit.getCreateBegin() != null && queryAudit.getCreateEnd() != null){
                predicates.add(cb.between(root.get(Audit.CREATE_TIME),queryAudit.getCreateBegin(),queryAudit.getCreateEnd()));
            }else if (queryAudit.getCreateBegin() != null){
                predicates.add(cb.greaterThan(root.get(Audit.CREATE_TIME),queryAudit.getCreateBegin()));
            }else if (queryAudit.getCreateEnd() != null){
                predicates.add(cb.lessThan(root.get(Audit.CREATE_TIME),queryAudit.getCreateEnd()));
            }
        }
        
        return predicates;
    }

    public static List<Predicate> initPredicatesByUser(QueryAudit queryAudit, Root<Audit> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        List<Predicate> predicates = initPredicates(queryAudit,root,query,cb);

        //用户与猎刃公共
        Subquery<Task> task = query.subquery(Task.class);
        Root<Task> tRoot = task.from(Task.class);
        task.select(tRoot.get(Task.ID));
        task.where(QueryUtils.equals(tRoot,cb,Task.USER_ID,queryAudit.userId));

        Subquery<AuditTask> auditTask = query.subquery(AuditTask.class);
        Root<AuditTask> atRoot = auditTask.from(AuditTask.class);
        auditTask.select(atRoot.get(AuditTask.AUDIT_ID));
        auditTask.where(QueryUtils.in(atRoot,cb,AuditTask.TASK_ID,task));

        Subquery<UserWithdraw> userWithdraw = query.subquery(UserWithdraw.class);
        Root<UserWithdraw> uwRoot = userWithdraw.from(UserWithdraw.class);
        userWithdraw.select(uwRoot.get(UserWithdraw.ID));
        userWithdraw.where(QueryUtils.equals(uwRoot,cb,UserWithdraw.USER_ID,queryAudit.userId));

        Subquery<AuditWithdraw> auditWithdraw = query.subquery(AuditWithdraw.class);
        Root<AuditWithdraw> awRoot = auditWithdraw.from(AuditWithdraw.class);
        auditWithdraw.select(awRoot.get(AuditWithdraw.AUDIT_ID));
        auditWithdraw.where(QueryUtils.in(awRoot,cb,AuditWithdraw.WITHDRAW_ID,userWithdraw));

        Predicate p1 = QueryUtils.in(root,cb,Audit.ID, auditTask);
        Predicate p2 = QueryUtils.in(root,cb,Audit.ID, auditWithdraw);


        if (queryAudit.getCategory().equals(UserCategory.HUNTER)){
            Subquery<AuditHunter> auditHunter = query.subquery(AuditHunter.class);
            Root<AuditHunter> ahRoot = auditHunter.from(AuditHunter.class);
            auditHunter.select(ahRoot.get(AuditWithdraw.AUDIT_ID));
            auditHunter.where(QueryUtils.equals(ahRoot,cb,AuditHunter.USER_ID,queryAudit.userId));

            Subquery<HunterTask> hunterTask = query.subquery(HunterTask.class);
            Root<HunterTask> htRoot = hunterTask.from(HunterTask.class);
            hunterTask.select(htRoot.get(HunterTask.ID));
            hunterTask.where(QueryUtils.equals(htRoot,cb,HunterTask.HUNTER_ID,queryAudit.userId));

            Subquery<AuditHunterTask> auditHunterTask = query.subquery(AuditHunterTask.class);
            Root<AuditHunterTask> ahtRoot = auditHunterTask.from(AuditHunterTask.class);
            auditHunterTask.select(ahtRoot.get(AuditHunterTask.AUDIT_ID));
            auditHunterTask.where(QueryUtils.in(ahtRoot,cb,AuditHunterTask.HUNTER_TASK_ID,hunterTask));

            Predicate p3 = QueryUtils.in(root,cb,Audit.ID, auditHunter);
            Predicate p4 = QueryUtils.in(root,cb,Audit.ID, auditHunterTask);
            predicates.add(cb.or(p1,p2,p3,p4));
        }else {
            predicates.add(cb.or(p1,p2));
        }

        predicates.removeIf(Objects::isNull);
        return predicates;
    }

}
