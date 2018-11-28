package com.tc.db.entity;

import com.tc.db.entity.pk.TaskStepPK;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Cyg
 * 任务步骤实体
 */
@Entity
@Table(name = "task_step", schema = "tc-company")
@IdClass(TaskStepPK.class)
public class TaskStep implements Serializable {
    private Integer step;
    private String title;
    private String context;
    private String img;
    private Task task;

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public Task getTask() {
        return task;
    }

    public void setTask(Task taskByTaskId) {
        this.task = taskByTaskId;
    }

    @Id
    @Column(name = "step")
    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "img")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        TaskStep taskStep = (TaskStep) o;
        return step.equals(taskStep.getStep()) &&
                Objects.equals(task.getId(), taskStep.getTask().getId()) &&
                Objects.equals(title, taskStep.title) &&
                Objects.equals(context, taskStep.context) &&
                Objects.equals(img, taskStep.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task.getId(), step, title, context, img);
    }


}
