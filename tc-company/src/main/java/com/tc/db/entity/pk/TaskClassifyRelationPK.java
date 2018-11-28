package com.tc.db.entity.pk;

import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassify;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TaskClassifyRelationPK implements Serializable {
    private TaskClassify taskClassify;
    private Task task;

    @Column(name = "task_classify_id")
    @Id
    public TaskClassify getTaskClassify() {
        return taskClassify;
    }

    public void setTaskClassify(TaskClassify taskClassifyByTaskClassifyId) {
        this.taskClassify = taskClassifyByTaskClassifyId;
    }

    @Column(name = "task_id")
    @Id
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        TaskClassifyRelationPK that = (TaskClassifyRelationPK) o;
        return taskClassify.getId().equals(that.getTaskClassify().getId()) &&
                Objects.equals(task.getId(), that.task.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskClassify.getId(), task.getId());
    }
}
