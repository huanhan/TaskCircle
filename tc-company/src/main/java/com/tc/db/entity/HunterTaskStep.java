package com.tc.db.entity;

import com.tc.db.entity.pk.HunterTaskStepPK;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Cyg
 * 猎刃接取任务的任务步骤完成详情实体
 */
@Entity
@Table(name = "hunter_task_step", schema = "tc-company")
@IdClass(value = HunterTaskStepPK.class)
public class HunterTaskStep implements Serializable {
    private String hunterTaskId;
    private Integer step;
    private Timestamp finishTime;
    private String context;
    private String remake;
    private HunterTask hunterTask;

    @Id
    @Column(name = "hunter_task_id")
    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_task_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public HunterTask getHunterTask() {
        return hunterTask;
    }

    public void setHunterTask(HunterTask hunterTaskByHunterTaskId) {
        this.hunterTask = hunterTaskByHunterTaskId;
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
    @Column(name = "finish_time")
    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
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
    @Column(name = "remake")
    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        HunterTaskStep that = (HunterTaskStep) o;
        return step.equals(that.getStep()) &&
                Objects.equals(hunterTask.getId(), that.getHunterTask().getId()) &&
                Objects.equals(finishTime, that.finishTime) &&
                Objects.equals(context, that.context) &&
                Objects.equals(remake, that.remake);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hunterTask.getId(), step, finishTime, context, remake);
    }


}
