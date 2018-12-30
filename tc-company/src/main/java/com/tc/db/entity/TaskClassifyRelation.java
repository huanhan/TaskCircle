package com.tc.db.entity;

import com.tc.db.entity.pk.TaskClassifyRelationPK;

import javax.persistence.*;
import java.util.Objects;

/**
 * 任务与任务分类关系实体
 * @author Cyg
 */
@Entity
@Table(name = "task_classify_relation", schema = "tc-company")
@IdClass(TaskClassifyRelationPK.class)
public class TaskClassifyRelation {
    private Long taskClassifyId;
    private String taskId;
    private TaskClassify taskClassify;
    private Task task;

    @Id
    @Column(name = "task_classify_id")
    public Long getTaskClassifyId() {
        return taskClassifyId;
    }

    public void setTaskClassifyId(Long taskClassifyId) {
        this.taskClassifyId = taskClassifyId;
    }

    @Id
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskClassifyRelation that = (TaskClassifyRelation) o;
        return taskClassifyId.equals(that.taskClassifyId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskClassifyId, taskId);
    }

    @ManyToOne
    @JoinColumn(name = "task_classify_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public TaskClassify getTaskClassify() {
        return taskClassify;
    }

    public void setTaskClassify(TaskClassify taskClassify) {
        this.taskClassify = taskClassify;
    }

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
