package com.tc.db.entity.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class HtsRecordPK implements Serializable {
    private String hunterTaskId;
    private Integer step;
    private Timestamp createTime;

    public HtsRecordPK() {
    }

    public HtsRecordPK(String hunterTaskId, Integer step, Timestamp createTime) {
        this.hunterTaskId = hunterTaskId;
        this.step = step;
        this.createTime = createTime;
    }

    @Column(name = "hunter_task_id")
    @Id
    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    @Column(name = "step")
    @Id
    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Column(name = "create_time")
    @Id
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HtsRecordPK that = (HtsRecordPK) o;
        return step.equals(that.step) &&
                Objects.equals(hunterTaskId, that.hunterTaskId) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(hunterTaskId, step, createTime);
    }
}
