package com.tc.dto.app;

import com.tc.db.entity.HunterTask;
import com.tc.db.enums.HunterTaskState;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

public class HunterTaskAppDto {
    private String id;
    private String taskId;
    private Long userId;
    private Long hunterId;
    private String hunterName;
    private String headImg;
    private String hunterHeadImg;
    private String name;
    private String taskContext;
    private String context;
    private Timestamp acceptTime;
    private Timestamp finishTime;
    private Timestamp beginTime;
    private HunterTaskState state;
    private Boolean isStop;
    private Float money;
    private Integer curStep;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getHunterName() {
        return hunterName;
    }

    public void setHunterName(String hunterName) {
        this.hunterName = hunterName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(String taskContext) {
        this.taskContext = taskContext;
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

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public HunterTaskState getState() {
        return state;
    }

    public void setState(HunterTaskState state) {
        this.state = state;
    }

    public Boolean getStop() {
        return isStop;
    }

    public void setStop(Boolean stop) {
        isStop = stop;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getCurStep() {
        return curStep;
    }

    public void setCurStep(Integer curStep) {
        this.curStep = curStep;
    }

    public String getHunterHeadImg() {
        return hunterHeadImg;
    }

    public void setHunterHeadImg(String hunterHeadImg) {
        this.hunterHeadImg = hunterHeadImg;
    }

    public Long getHunterId() {
        return hunterId;
    }

    public void setHunterId(Long hunterId) {
        this.hunterId = hunterId;
    }

    public static HunterTaskAppDto toDetail(HunterTask task) {
        HunterTaskAppDto hunterTaskAppDto = new HunterTaskAppDto();
        hunterTaskAppDto.setUserId(task.getTask().getUserId());
        hunterTaskAppDto.setHunterId(task.getHunterId());
        hunterTaskAppDto.setHunterName(task.getHunter().getUser().getName());
        hunterTaskAppDto.setHeadImg(task.getTask().getUser().getHeadImg());
        hunterTaskAppDto.setHunterHeadImg(task.getHunter().getUser().getHeadImg());
        hunterTaskAppDto.setName(task.getTask().getName());
        hunterTaskAppDto.setTaskContext(task.getTask().getContext());
        hunterTaskAppDto.setCurStep(task.getHunterTaskSteps().size());
        BeanUtils.copyProperties(task, hunterTaskAppDto);
        return hunterTaskAppDto;
    }
}
