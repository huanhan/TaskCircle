package com.tc.dto.condition;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Condition;
import com.tc.db.entity.User;
import com.tc.db.entity.Condition;
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
 * 查询消息的查询条件
 * @author Cyg
 */
public class QueryCondition extends PageRequest {

    private Long id;
    private String context;
    private String name;
    private String value;
    private Timestamp creationTimeBegin;
    private Timestamp creationTimeEnd;
    private Long adminId;
    private String adminName;
    private String account;

    public QueryCondition() {
        super(0,10);
    }

    public QueryCondition(int page, int size) {
        super(page, size);
    }

    public QueryCondition(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryCondition(int page, int size, Sort sort) {
        super(page, size, sort);
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getCreationTimeBegin() {
        return creationTimeBegin;
    }

    public void setCreationTimeBegin(Timestamp creationTimeBegin) {
        this.creationTimeBegin = creationTimeBegin;
    }

    public Timestamp getCreationTimeEnd() {
        return creationTimeEnd;
    }

    public void setCreationTimeEnd(Timestamp creationTimeEnd) {
        this.creationTimeEnd = creationTimeEnd;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public static List<Predicate> initPredicates(QueryCondition queryCondition, Root<Condition> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(QueryUtils.equals(root,cb,Condition.ID,queryCondition.id));
        predicates.add(QueryUtils.equals(root,cb,Condition.ADMIN_ID,queryCondition.adminId));
        predicates.add(QueryUtils.equals(root,cb,Condition.NAME,queryCondition.name));
        predicates.add(QueryUtils.equals(root.get(Condition.ADMIN).get(Admin.USER).get(User.NAME),cb,queryCondition.adminName));
        predicates.add(QueryUtils.equals(root.get(Condition.NAME).get(Admin.USER).get(User.USERNAME),cb,queryCondition.account));

        predicates.add(QueryUtils.like(root,cb,Condition.CONTEXT,queryCondition.context));
        predicates.add(QueryUtils.like(root,cb,Condition.VALUE,queryCondition.value));

        predicates.add(QueryUtils.between(root, cb, Condition.CREATION_NEME, queryCondition.creationTimeBegin, queryCondition.creationTimeEnd));

        predicates.removeIf(Objects::isNull);

        return predicates;
    }
}
