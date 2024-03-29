package com.tc.dto.finance;

import com.tc.db.entity.User;
import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.DateType;
import com.tc.db.enums.WithdrawState;
import com.tc.db.enums.WithdrawType;
import com.tc.dto.TimeScope;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 收支记录查询条件
 * @author Cyg
 */
public class QueryFinance extends PageRequest {

    private String id;
    private Long userId;
    private Long account;
    private Long username;
    private Float moneyBegin;
    private Float moneyEnd;
    private WithdrawState state;
    private WithdrawType type;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    @NotNull
    private Timestamp auditPassBegin;
    @NotNull
    private Timestamp auditPassEnd;
    private List<WithdrawState> states;


    /**
     * 设置查看方式用的
     */
    @NotNull
    private DateType dateType;


    public QueryFinance() {
        super(0,10);
    }

    public QueryFinance(int page, int size) {
        super(page, size);
    }

    public QueryFinance(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryFinance(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public QueryFinance(Long id, Timestamp begin, Timestamp end) {
        super(0,10);
        this.userId = id;
        this.auditPassBegin = begin;
        this.auditPassEnd = end;
        this.state = WithdrawState.SUCCESS;
    }

    public QueryFinance(Timestamp begin, Timestamp end) {
        super(0,10);
        this.auditPassBegin = begin;
        this.auditPassEnd = end;
        this.state = WithdrawState.SUCCESS;
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

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Long getUsername() {
        return username;
    }

    public void setUsername(Long username) {
        this.username = username;
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

    public WithdrawState getState() {
        return state;
    }

    public void setState(WithdrawState state) {
        this.state = state;
    }

    public WithdrawType getType() {
        return type;
    }

    public void setType(WithdrawType type) {
        this.type = type;
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

    public Timestamp getAuditPassBegin() {
        return auditPassBegin;
    }

    public void setAuditPassBegin(Timestamp auditPassBegin) {
        this.auditPassBegin = auditPassBegin;
    }

    public Timestamp getAuditPassEnd() {
        return auditPassEnd;
    }

    public void setAuditPassEnd(Timestamp auditPassEnd) {
        this.auditPassEnd = auditPassEnd;
    }

    public DateType getDateType() {
        return dateType;
    }

    public void setDateType(DateType dateType) {
        this.dateType = dateType;
    }

    public List<WithdrawState> getStates() {
        return states;
    }

    public void setStates(List<WithdrawState> states) {
        this.states = states;
    }

    public static List<Predicate> initPredicates(QueryFinance queryFinance, Root<UserWithdraw> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.equals(root,cb,UserWithdraw.ID,queryFinance.id));
        predicates.add(QueryUtils.equals(root,cb,UserWithdraw.USER_ID,queryFinance.userId));
        predicates.add(QueryUtils.equals(root,cb,UserWithdraw.STATE,queryFinance.state));
        predicates.add(QueryUtils.equals(root,cb,UserWithdraw.TYPE,queryFinance.type));
        predicates.add(QueryUtils.equals(root.get(UserWithdraw.USER).get(User.NAME),cb,queryFinance.username));
        predicates.add(QueryUtils.equals(root.get(UserWithdraw.USER).get(User.USERNAME),cb,queryFinance.account));

        predicates.add(QueryUtils.between(root,cb,User.MONEY,queryFinance.getMoneyBegin(),queryFinance.getMoneyEnd()));
        predicates.add(QueryUtils.between(root, cb, UserWithdraw.CREATE_TIME, queryFinance.getCreateTimeBegin(), queryFinance.getCreateTimeEnd()));
        predicates.add(QueryUtils.between(root, cb, UserWithdraw.AUDIT_PASS_TIME, queryFinance.getAuditPassBegin(), queryFinance.getAuditPassEnd()));
        predicates.add(QueryUtils.in(root,cb,UserWithdraw.STATE,queryFinance.states));
        predicates.removeIf(Objects::isNull);

        return predicates;
    }

    public static List<Predicate> initPredicatesBy(TimeScope scope, Root<UserWithdraw> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(QueryUtils.equals(root,cb,UserWithdraw.USER_ID,scope.getId()));
        predicates.add(cb.equal(root.get(UserWithdraw.STATE),WithdrawState.AUDIT));
        predicates.add(cb.isNotNull(root.get(UserWithdraw.CREATE_TIME)));
        predicates.add(QueryUtils.between(root, cb, UserWithdraw.CREATE_TIME, scope.getBegin(), scope.getEnd()));
        predicates.removeIf(Objects::isNull);
        return predicates;
    }

    public static QueryFinance init(Long id, Timestamp begin, Timestamp end) {
        return new QueryFinance(id,begin,end);
    }

    public static QueryFinance init(Timestamp begin, Timestamp end) {
        return new QueryFinance(begin,end);
    }
}
