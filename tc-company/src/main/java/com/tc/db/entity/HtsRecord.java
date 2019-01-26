package com.tc.db.entity;

import com.google.gson.Gson;
import com.tc.db.entity.pk.HtsRecordPK;
import com.tc.db.enums.OPType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "hts_record", schema = "tc-company")
@IdClass(HtsRecordPK.class)
public class HtsRecord {
    private String hunterTaskId;
    private int step;
    private Timestamp createTime;
    private String originalContext;
    private String afterContext;
    private OPType operation;
    private HunterTaskStep hunterTaskStep;

    @Id
    @Column(name = "hunter_task_id")
    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    @Id
    @Column(name = "step")
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Id
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "original_context")
    public String getOriginalContext() {
        return originalContext;
    }

    public void setOriginalContext(String originalContext) {
        this.originalContext = originalContext;
    }

    @Basic
    @Column(name = "after_context")
    public String getAfterContext() {
        return afterContext;
    }

    public void setAfterContext(String afterContext) {
        this.afterContext = afterContext;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    public OPType getOperation() {
        return operation;
    }

    public void setOperation(OPType operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HtsRecord htsRecord = (HtsRecord) o;
        return step == htsRecord.step &&
                Objects.equals(hunterTaskId, htsRecord.hunterTaskId) &&
                Objects.equals(createTime, htsRecord.createTime) &&
                Objects.equals(originalContext, htsRecord.originalContext) &&
                Objects.equals(afterContext, htsRecord.afterContext) &&
                Objects.equals(operation, htsRecord.operation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(hunterTaskId, step, createTime, originalContext, afterContext, operation);
    }

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "hunter_task_id", referencedColumnName = "hunter_task_id", nullable = false,insertable = false,updatable = false),
            @JoinColumn(name = "step", referencedColumnName = "step", nullable = false,insertable = false,updatable = false)})
    public HunterTaskStep getHunterTaskStep() {
        return hunterTaskStep;
    }

    public void setHunterTaskStep(HunterTaskStep hunterTaskStep) {
        this.hunterTaskStep = hunterTaskStep;
    }


    public static String setContext(HunterTaskStep hunterTaskStep){
        if (hunterTaskStep == null){
            return null;
        }
        try {
            return new Gson().toJson(hunterTaskStep);
        }catch (Exception e){
            return null;
        }
    }

}
