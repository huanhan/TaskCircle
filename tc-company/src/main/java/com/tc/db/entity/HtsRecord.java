package com.tc.db.entity;

import com.google.gson.Gson;
import com.tc.db.entity.pk.HtsRecordPK;
import com.tc.db.enums.OPType;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "hts_record", schema = "tc-company")
@IdClass(HtsRecordPK.class)
public class HtsRecord {

    public static final String HUNTER_TASK_ID = "hunterTaskId";
    public static final String STEP = "step";
    public static final String CREATE_TIME = "createTime";
    public static final String OPERATION = "operation";
    public static final String HUNTER_TASK_STEP = "hunterTaskStep";

    private String hunterTaskId;
    private Integer step;
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
    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Id
    @CreationTimestamp
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
    @JoinColumns({@JoinColumn(name = "hunter_task_id", referencedColumnName = "hunter_task_id", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "step", referencedColumnName = "step", nullable = false, insertable = false, updatable = false)})
    public HunterTaskStep getHunterTaskStep() {
        return hunterTaskStep;
    }

    public void setHunterTaskStep(HunterTaskStep hunterTaskStep) {
        this.hunterTaskStep = hunterTaskStep;
    }


    public static String setContext(HunterTaskStep hunterTaskStep) {
        if (hunterTaskStep == null) {
            return null;
        }
        try {
            return new Gson().toJson(hunterTaskStep);
        } catch (Exception e) {
            return null;
        }
    }

    public static HunterTaskStep getContext(String jsonContext) {
        try {
            return new Gson().fromJson(jsonContext, HunterTaskStep.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<HtsRecord> toListInIndex(List<HtsRecord> records) {
        if (ListUtils.isNotEmpty(records)) {
            records.forEach(htsRecord -> {
                if (htsRecord.hunterTaskStep != null) {
                    HunterTaskStep hunterTaskStep = htsRecord.hunterTaskStep;
                    htsRecord.hunterTaskStep = HunterTaskStep.toDetail(hunterTaskStep);
                }
            });
        }
        return records;
    }

}
