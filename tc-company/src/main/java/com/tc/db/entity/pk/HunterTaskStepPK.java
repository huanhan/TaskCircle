package com.tc.db.entity.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 */
public class HunterTaskStepPK implements Serializable {
    private String hunterTaskId;
    private Integer step;

    public HunterTaskStepPK() {
    }

    public HunterTaskStepPK(String id, Integer step) {
        this.hunterTaskId = id;
        this.step = step;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        HunterTaskStepPK that = (HunterTaskStepPK) o;
        return step.equals(that.getStep()) &&
                hunterTaskId.equals(that.getHunterTaskId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(hunterTaskId, step);
    }
}
