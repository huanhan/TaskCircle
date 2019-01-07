package com.tc.dto.task;

import com.tc.db.entity.Admin;
import com.tc.db.entity.TaskClassify;
import com.tc.db.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询任务分类
 * @author Cyg
 */
public class QueryTaskClassify extends PageRequest {

    private String name;
    private String info;
    private String creationName;
    private String parentName;
    private Boolean existTask;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;

    public QueryTaskClassify() {
        super(0,10);
    }

    public QueryTaskClassify(int page, int size) {
        super(page, size);
    }

    public QueryTaskClassify(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryTaskClassify(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean getExistTask() {
        return existTask;
    }

    public void setExistTask(Boolean existTask) {
        this.existTask = existTask;
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

    public static List<Predicate> initPredicates(QueryTaskClassify queryTaskClassify, Root<TaskClassify> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(queryTaskClassify.name)){
            predicates.add(cb.equal(root.get(TaskClassify.NAME),queryTaskClassify.name));
        }
        if (!StringUtils.isEmpty(queryTaskClassify.info)){
            predicates.add(cb.like(root.get(TaskClassify.INFO),"%" + queryTaskClassify.info + "%"));
        }
        if (!StringUtils.isEmpty(queryTaskClassify.creationName)){
            predicates.add(cb.equal(root.get(TaskClassify.CREATION).get(Admin.USER).get(User.NAME),queryTaskClassify.creationName));
        }
        if (!StringUtils.isEmpty(queryTaskClassify.parentName)){
            predicates.add(cb.equal(root.get(TaskClassify.PARENTS).get(TaskClassify.NAME),queryTaskClassify.parentName));
        }
        if (queryTaskClassify.getExistTask() != null){
            if (queryTaskClassify.getExistTask()) {
                predicates.add(cb.isEmpty(root.get(TaskClassify.TASK_CLASSIFY_RELATIONS)));
            }else {
                predicates.add(cb.isNotEmpty(root.get(TaskClassify.TASK_CLASSIFY_RELATIONS)));
            }
        }
        if (queryTaskClassify.getCreateTimeBegin() != null || queryTaskClassify.getCreateTimeEnd() != null){
            if (queryTaskClassify.getCreateTimeBegin() != null && queryTaskClassify.getCreateTimeEnd() != null){
                predicates.add(cb.between(root.get(TaskClassify.CREATE_TIME),queryTaskClassify.getCreateTimeBegin(),queryTaskClassify.getCreateTimeEnd()));
            }else if (queryTaskClassify.getCreateTimeBegin() != null){
                predicates.add(cb.greaterThan(root.get(TaskClassify.CREATE_TIME),queryTaskClassify.getCreateTimeBegin()));
            }else if (queryTaskClassify.getCreateTimeEnd() != null){
                predicates.add(cb.lessThan(root.get(TaskClassify.CREATE_TIME),queryTaskClassify.getCreateTimeEnd()));
            }
        }
        return predicates;
    }
}
