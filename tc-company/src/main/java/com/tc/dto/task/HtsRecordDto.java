package com.tc.dto.task;

import com.tc.db.entity.HtsRecord;
import com.tc.db.entity.HunterTaskStep;
import com.tc.db.enums.OPType;

import java.sql.Timestamp;

/**
 *
 * @author Cyg
 */
public class HtsRecordDto {
    private String hunterTaskId;
    private Integer step;
    private Timestamp createTime;
    private HunterTaskStep original;
    private HunterTaskStep after;
    private HunterTaskStep current;
    private OPType operation;
    public static HtsRecordDto by(HtsRecord record) {
        HtsRecordDto result = new HtsRecordDto();
        if (record != null){
            result.setHunterTaskId(record.getHunterTaskId());
            result.setStep(record.getStep());
            result.setCreateTime(record.getCreateTime());
            result.setOperation(record.getOperation());
            result.setOriginal(HtsRecord.getContext(record.getOriginalContext()));
            result.setAfter(HtsRecord.getContext(record.getAfterContext()));
            result.setCurrent(HunterTaskStep.toDetail(record.getHunterTaskStep()));
        }
        return result;
    }

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public HunterTaskStep getOriginal() {
        return original;
    }

    public void setOriginal(HunterTaskStep original) {
        this.original = original;
    }

    public HunterTaskStep getAfter() {
        return after;
    }

    public void setAfter(HunterTaskStep after) {
        this.after = after;
    }

    public HunterTaskStep getCurrent() {
        return current;
    }

    public void setCurrent(HunterTaskStep current) {
        this.current = current;
    }

    public OPType getOperation() {
        return operation;
    }

    public void setOperation(OPType operation) {
        this.operation = operation;
    }
}
