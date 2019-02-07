package com.tc.dto.app;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class HunterCommentDto {

    //评价任务的内容
    @NotEmpty(message = "评价任务内容不能为空")
    private String taskContext;

    //任务的星星数
    @NotNull(message = "评分不能为空")
    private Float taskStart;

    //被评价的猎刃任务id
    @NotEmpty(message = "任务id不能为空")
    private String hunterTaskId;

    //评价用户的内容
    @NotEmpty(message = "评价用户内容不能为空")
    private String userContext;

    //用户的星星数
    @NotNull(message = "评分不能为空")
    private Float userStart;

    public String getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(String taskContext) {
        this.taskContext = taskContext;
    }

    public Float getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(Float taskStart) {
        this.taskStart = taskStart;
    }

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    public String getUserContext() {
        return userContext;
    }

    public void setUserContext(String userContext) {
        this.userContext = userContext;
    }

    public Float getUserStart() {
        return userStart;
    }

    public void setUserStart(Float userStart) {
        this.userStart = userStart;
    }
}
