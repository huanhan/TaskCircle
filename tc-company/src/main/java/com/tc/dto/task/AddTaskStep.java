package com.tc.dto.task;


import com.tc.db.entity.TaskStep;



/**
 * 添加任务步骤
 * @author Cyg
 */
public class AddTaskStep {

    private String taskId;
    private Integer step;
    private String title;
    private String context;
    private String img;

    public static TaskStep toTaskStep(AddTaskStep addTaskStep) {
        TaskStep taskStep = new TaskStep();
        taskStep.setStep(addTaskStep.step);
        taskStep.setContext(addTaskStep.context);
        taskStep.setImg(addTaskStep.img);
        taskStep.setTaskId(addTaskStep.taskId);
        taskStep.setTitle(addTaskStep.title);
        return taskStep;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
