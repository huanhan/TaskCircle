package com.tc.dto.app;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.HunterTaskStep;
import com.tc.db.entity.Task;
import com.tc.db.entity.TaskStep;
import com.tc.db.enums.HunterTaskState;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HunterTaskRunningStateDto {

    private Long userId;//用户id
    private String taskId;//任务id
    private String hunterTaskId;//猎刃任务id
    private Long hunterId;//猎刃id
    private String name;//任务名
    private String context;//任务简介
    private Timestamp acceptTime;//接取时间
    private Timestamp beginTime;//可执行开始时间
    private Timestamp deadline;//可执行截至时间
    private Timestamp taskFinishTime;//任务完成时间
    private Timestamp auditTime;//提交审核时间
    private Timestamp taskBeginTime;//任务开始时间
    private Integer currStep;//当前步骤
    private Integer totalStep;//所有步骤数
    private HunterTaskState state;//任务状态
    private Boolean isStop;//是否暂停

    private Collection<HunterRunningStepDto> taskSteps;

    public static HunterTaskRunningStateDto toDetail(Task task, HunterTask hunterTask) {
        HunterTaskRunningStateDto hunterTaskRunningStateDto = new HunterTaskRunningStateDto();

        hunterTaskRunningStateDto.setUserId(task.getUserId());
        hunterTaskRunningStateDto.setTaskId(task.getId());
        hunterTaskRunningStateDto.setHunterTaskId(hunterTask.getId());
        hunterTaskRunningStateDto.setHunterId(hunterTask.getHunterId());
        hunterTaskRunningStateDto.setName(task.getName());
        hunterTaskRunningStateDto.setContext(task.getContext());
        hunterTaskRunningStateDto.setAcceptTime(hunterTask.getAcceptTime());
        hunterTaskRunningStateDto.setBeginTime(task.getBeginTime());
        hunterTaskRunningStateDto.setDeadline(task.getDeadline());
        hunterTaskRunningStateDto.setTaskFinishTime(hunterTask.getFinishTime());
        hunterTaskRunningStateDto.setAuditTime(hunterTask.getAuditTime());
        hunterTaskRunningStateDto.setTaskBeginTime(hunterTask.getBeginTime());
        hunterTaskRunningStateDto.setCurrStep(hunterTask.getHunterTaskSteps().size());
        hunterTaskRunningStateDto.setTotalStep(task.getTaskSteps().size());
        hunterTaskRunningStateDto.setTaskBeginTime(hunterTask.getBeginTime());
        hunterTaskRunningStateDto.setState(hunterTask.getState());
        hunterTaskRunningStateDto.setState(hunterTask.getState());
        hunterTaskRunningStateDto.setStop(hunterTask.getStop());

        //获取任务步骤和执行步骤的结合体
        Collection<HunterTaskStep> hunterTaskSteps = hunterTask.getHunterTaskSteps();
        Collection<TaskStep> taskSteps = task.getTaskSteps();

        List<HunterRunningStepDto> hunterRunningStepDtos = new ArrayList<>();
        HunterRunningStepDto hunterRunningStepDto;
        for (TaskStep taskStep : taskSteps) {

            hunterRunningStepDto = new HunterRunningStepDto();
            hunterRunningStepDto.setHunterTaskId(hunterTask.getId());
            hunterRunningStepDto.setStep(taskStep.getStep());
            hunterRunningStepDto.setTaskTitle(taskStep.getTitle());
            hunterRunningStepDto.setTaskContext(taskStep.getContext());
            hunterRunningStepDto.setTaskImg(taskStep.getImg());

            HunterTaskStep hunterTaskStep = getHunterStep(hunterTaskSteps, taskStep.getStep());

            if (hunterTaskStep != null) {
                hunterRunningStepDto.setHunterTaskFinishTime(hunterTaskStep.getFinishTime());
                hunterRunningStepDto.setHunterTaskContext(hunterTaskStep.getContext());
                hunterRunningStepDto.setHunterTaskRemake(hunterTaskStep.getRemake());
            }
            hunterRunningStepDtos.add(hunterRunningStepDto);

        }
        hunterTaskRunningStateDto.setTaskSteps(hunterRunningStepDtos);
        return hunterTaskRunningStateDto;
    }

    private static HunterTaskStep getHunterStep(Collection<HunterTaskStep> hunterTaskSteps, Integer step) {
        for (HunterTaskStep hunterTaskStep : hunterTaskSteps) {
            if (step == hunterTaskStep.getStep()) {
                //步骤数相同
                return hunterTaskStep;
            }
        }
        return null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Timestamp getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Timestamp acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Timestamp getTaskFinishTime() {
        return taskFinishTime;
    }

    public void setTaskFinishTime(Timestamp taskFinishTime) {
        this.taskFinishTime = taskFinishTime;
    }

    public Timestamp getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime) {
        this.auditTime = auditTime;
    }

    public Timestamp getTaskBeginTime() {
        return taskBeginTime;
    }

    public void setTaskBeginTime(Timestamp taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
    }

    public HunterTaskState getState() {
        return state;
    }

    public void setState(HunterTaskState state) {
        this.state = state;
    }

    public Integer getCurrStep() {
        return currStep;
    }

    public void setCurrStep(Integer currStep) {
        this.currStep = currStep;
    }

    public Integer getTotalStep() {
        return totalStep;
    }

    public void setTotalStep(Integer totalStep) {
        this.totalStep = totalStep;
    }

    public Collection<HunterRunningStepDto> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(Collection<HunterRunningStepDto> taskSteps) {
        this.taskSteps = taskSteps;
    }

    public Boolean getStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }
}
