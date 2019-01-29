package com.tc.dto.task;

import com.tc.db.entity.HtsRecord;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;
import com.tc.db.enums.OPType;
import com.tc.dto.TimeScope;
import com.tc.until.PageRequest;
import com.tc.until.QueryUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 查询步骤变更记录
 * @author Cyg
 */
public class QueryHtsRecords extends PageRequest {
    private String taskId;
    private Integer step;
    private String hunterTaskId;
    private Timestamp createTimeBegin;
    private Timestamp createTimeEnd;
    private OPType operation;

    public QueryHtsRecords() {
        super(0, 10);
    }

    public QueryHtsRecords(int page, int size) {
        super(page, size);
    }

    public QueryHtsRecords(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryHtsRecords(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
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

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public OPType getOperation() {
        return operation;
    }

    public void setOperation(OPType operation) {
        this.operation = operation;
    }

    public static List<Predicate> initPredicates(QueryHtsRecords queryHtsRecords, Root<HtsRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();


        if (StringUtils.isNotEmpty(queryHtsRecords.hunterTaskId)){
            predicates.add(QueryUtils.equals(root.get(HtsRecord.HUNTER_TASK_STEP).get(HunterTaskStep.HUNTER_TASK_ID),cb,queryHtsRecords.hunterTaskId));
        }else {
            predicates.add(QueryUtils.equals(root.get(HtsRecord.HUNTER_TASK_STEP).get(HunterTaskStep.HUNTER_TASK).get(HunterTask.TASK_ID),cb,queryHtsRecords.taskId));

        }
        predicates.add(QueryUtils.equals(root.get(HtsRecord.HUNTER_TASK_STEP).get(HunterTaskStep.STEP),cb,queryHtsRecords.step));
        predicates.add(QueryUtils.between(root,cb,HtsRecord.CREATE_TIME,queryHtsRecords.createTimeBegin,queryHtsRecords.createTimeEnd));
        predicates.add(QueryUtils.equals(root,cb,HtsRecord.OPERATION,queryHtsRecords.operation));
        predicates.removeIf(Objects::isNull);
        return predicates;
    }
}
