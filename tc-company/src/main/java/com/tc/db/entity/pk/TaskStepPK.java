package com.tc.db.entity.pk;


import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * 任务步骤主键
 * @author Cyg
 */
public class TaskStepPK implements Serializable {
    private String taskId;
    private Integer step;

    @Column(name = "task_id")
    @Id
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
                taskId.equals(that.getTaskId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskId, step);
    }
}
