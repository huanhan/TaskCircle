package com.tc.db.entity;

import com.tc.db.entity.pk.TaskClassifyRelationPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 任务联系关联实体
 */
@Entity
@Table(name = "task_classify_relation", schema = "tc-company")
@IdClass(TaskClassifyRelationPK.class)
public class TaskClassifyRelation implements Serializable {

    private TaskClassify taskClassify;
    private Task task;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        TaskClassifyRelation that = (TaskClassifyRelation) o;
        return taskClassify.getId().equals(that.getTaskClassify().getId()) &&
                Objects.equals(task.getId(), that.getTask().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskClassify.getId(), task.getId());
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "task_classify_id", referencedColumnName = "id", nullable = false)
    public TaskClassify getTaskClassify() {
        return taskClassify;
    }

    public void setTaskClassify(TaskClassify taskClassifyByTaskClassifyId) {
        this.taskClassify = taskClassifyByTaskClassifyId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }
}
