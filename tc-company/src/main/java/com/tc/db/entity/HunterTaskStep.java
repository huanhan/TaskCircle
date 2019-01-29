package com.tc.db.entity;

import com.tc.db.entity.pk.HunterTaskStepPK;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 猎刃接取任务的任务步骤完成详情实体
 */
@Entity
@Table(name = "hunter_task_step", schema = "tc-company", catalog = "")
@IdClass(value = HunterTaskStepPK.class)
public class HunterTaskStep implements Serializable {
    public static final String STEP = "step";
    public static final String HUNTER_TASK_ID = "hunterTaskId";
    public static final String HUNTER_TASK = "hunterTask";

    private String hunterTaskId;
    private Integer step;
    private Timestamp finishTime;
    private String context;
    private String remake;
    private HunterTask hunterTask;
    private Collection<HtsRecord> htsRecords;

    public static List<HunterTaskStep> toListInIndex(List<HunterTaskStep> query) {
        if (ListUtils.isNotEmpty(query)){

            query.forEach(hunterTaskStep -> {
                if (hunterTaskStep.getHunterTask() != null){
                    hunterTaskStep.setHunterTask(HunterTask.toDetail(hunterTaskStep.getHunterTask()));
                }
            });
        }
        return query;
    }

    public static HunterTaskStep toDetail(HunterTaskStep hunterTaskStep) {
        if (hunterTaskStep != null){
            if (hunterTaskStep.getHunterTask() != null){
                hunterTaskStep.hunterTask = new HunterTask(hunterTaskStep.getHunterTaskId(),hunterTaskStep.getHunterTask().getTask());
            }
        }
        return hunterTaskStep;
    }

    @Id
    @Column(name = "hunter_task_id")
    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    @ManyToOne
    @JoinColumn(name = "hunter_task_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
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
    @CreationTimestamp
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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

    @OneToMany(mappedBy = "hunterTaskStep")
    public Collection<HtsRecord> getHtsRecords() {
        return htsRecords;
    }

    public void setHtsRecords(Collection<HtsRecord> htsRecords) {
        this.htsRecords = htsRecords;
    }
}
