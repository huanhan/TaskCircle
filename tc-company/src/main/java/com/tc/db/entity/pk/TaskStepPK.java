package com.tc.db.entity.pk;

import com.tc.db.entity.Task;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TaskStepPK implements Serializable {
    private Task task;
    private Integer step;

    @Column(name = "task_id")
    @Id
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
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
        TaskStepPK that = (TaskStepPK) o;
        return step.equals(that.getStep()) &&
                Objects.equals(task.getId(), that.getTask().getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(task.getId(), step);
    }
}
