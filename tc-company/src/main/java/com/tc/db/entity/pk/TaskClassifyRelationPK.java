package com.tc.db.entity.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * 任务与任务分类关系主键
 * @author Cyg
 */
public class TaskClassifyRelationPK implements Serializable {
    private Long taskClassifyId;
    private String taskId;

    @Column(name = "task_classify_id")
    @Id
    public Long getTaskClassifyId() {
        return taskClassifyId;
    }

    public void setTaskClassifyId(Long taskClassifyId) {
        this.taskClassifyId = taskClassifyId;
    }

    @Column(name = "task_id")
    @Id
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
        TaskClassifyRelationPK that = (TaskClassifyRelationPK) o;
        return taskClassifyId.equals(that.taskClassifyId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskClassifyId, taskId);
    }
}
