package com.tc.dto.app;

import java.sql.Timestamp;

public class HunterRunningStepDto {

    private String hunterTaskId;
    private Integer step;
    private Timestamp hunterTaskFinishTime;
    private String hunterTaskContext;
    private String hunterTaskRemake;
    private String taskTitle;
    private String taskContext;
    private String taskImg;

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Timestamp getHunterTaskFinishTime() {
        return hunterTaskFinishTime;
    }

    public void setHunterTaskFinishTime(Timestamp hunterTaskFinishTime) {
        this.hunterTaskFinishTime = hunterTaskFinishTime;
    }

    public String getHunterTaskContext() {
        return hunterTaskContext;
    }

    public void setHunterTaskContext(String hunterTaskContext) {
        this.hunterTaskContext = hunterTaskContext;
    }

    public String getHunterTaskRemake() {
        return hunterTaskRemake;
    }

    public void setHunterTaskRemake(String hunterTaskRemake) {
        this.hunterTaskRemake = hunterTaskRemake;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(String taskContext) {
        this.taskContext = taskContext;
    }

    public String getTaskImg() {
        return taskImg;
    }

    public void setTaskImg(String taskImg) {
        this.taskImg = taskImg;
    }
}
