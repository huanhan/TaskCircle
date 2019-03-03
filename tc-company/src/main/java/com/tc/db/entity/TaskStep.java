package com.tc.db.entity;

import com.tc.db.entity.pk.TaskStepPK;
import com.tc.dto.task.AddTaskStep;
import com.tc.dto.task.step.HTStep;
import com.tc.dto.trans.Trans;
import com.tc.until.ListUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Cyg
 * 任务步骤实体
 */
@Entity
@Table(name = "task_step", schema = "tc-company")
@IdClass(TaskStepPK.class)
public class TaskStep implements Serializable {

    public static final String STEP = "step";

    private String taskId;
    private Integer step;
    private String title;
    private String context;
    private String img;
    private Task task;

    private List<HTStep> htSteps;


    @Id
    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
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

    @Transient
    public List<HTStep> getHtSteps() {
        return htSteps;
    }

    public void setHtSteps(List<HTStep> htSteps) {
        this.htSteps = htSteps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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

    public static TaskStep toDetail(TaskStep taskStep) {
        if (taskStep != null) {
            if (taskStep.getTask() != null) {
                taskStep.setTask(new Task(taskStep.getTask().getId(), taskStep.getTask().getName()));
            }
        }
        return taskStep;
    }

    public static List<TaskStep> toIndexByList(List<TaskStep> queryTss) {
        if (!ListUtils.isEmpty(queryTss)) {
            queryTss.forEach(ts -> {
                if (ts.getTask() != null) {
                    ts.setTask(new Task(ts.getTask().getId(), ts.getTask().getName()));
                }
            });
        }
        return queryTss;
    }

    public static List<TaskStep> op(List<TaskStep> taskSteps, List<HunterTask> hunterTasks) {
        if (ListUtils.isNotEmpty(taskSteps)){
            taskSteps.forEach(taskStep -> {
                taskStep.htSteps = new ArrayList<>();
                if (taskStep.getTask() != null) {
                    taskStep.setTask(new Task(taskStep.getTask().getId(), taskStep.getTask().getName()));
                }
            });
            if (ListUtils.isNotEmpty(hunterTasks)) {
                hunterTasks.forEach(hunterTask -> {
                    User user = hunterTask.getHunter().getUser();
                    if (ListUtils.isNotEmpty(hunterTask.getHunterTaskSteps())) {
                        taskSteps.forEach(taskStep -> {
                            AtomicBoolean isExist = new AtomicBoolean(false);
                            hunterTask.getHunterTaskSteps().forEach(hunterTaskStep -> {
                                if (hunterTaskStep.getStep().equals(taskStep.step)) {
                                    taskStep.htSteps.add(new HTStep(
                                            user.getId(),
                                            user.getUsername(),
                                            user.getName(),
                                            hunterTaskStep.getHunterTaskId(),
                                            hunterTaskStep.getStep(),
                                            new Trans("OK","执行完成")
                                    ));
                                    isExist.set(true);
                                }
                            });
                            if (!isExist.get()){
                                taskStep.htSteps.add(new HTStep(
                                        user.getId(),
                                        user.getUsername(),
                                        user.getName(),
                                        new Trans("NO","未开始执行")
                                ));
                            }
                        });
                    }else {
                        taskSteps.forEach(taskStep -> taskStep.htSteps.add(new HTStep(
                                user.getId(),
                                user.getUsername(),
                                user.getName(),
                                new Trans("NO","未开始执行")
                        )));
                    }
                });
            }
        }
        return taskSteps;
    }

    public static Collection<TaskStep> toList(String id, List<AddTaskStep> taskSteps) {
        List<TaskStep> result = new ArrayList<>();
        if (!ListUtils.isEmpty(taskSteps)) {

            taskSteps.forEach(ts -> {
                ts.setTaskId(id);
                result.add(AddTaskStep.toTaskStep(ts));
            });

        }
        return result;
    }
}
