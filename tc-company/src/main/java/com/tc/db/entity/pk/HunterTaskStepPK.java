package com.tc.db.entity.pk;

import com.tc.db.entity.HunterTask;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class HunterTaskStepPK implements Serializable {
    private HunterTask hunterTask;
    private Integer step;

    @Column(name = "hunter_task_id")
    @Id
    public HunterTask getHunterTask() {
        return hunterTask;
    }

    public void setHunterTask(HunterTask hunterTaskByHunterTaskId) {
        this.hunterTask = hunterTaskByHunterTaskId;
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
                Objects.equals(hunterTask.getId(), that.getHunterTask().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(hunterTask.getId(), step);
    }
}
