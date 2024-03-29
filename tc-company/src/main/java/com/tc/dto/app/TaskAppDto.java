package com.tc.dto.app;

import com.tc.db.entity.AuditTask;
import com.tc.db.entity.Task;
import com.tc.db.entity.TaskStep;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

public class TaskAppDto {
    private String id;
    private Long userId;
    private String name;
    private String context;
    private String username;
    private String headImg;
    private Float money;
    private Float originalMoney;
    private TaskState state;
    private TaskType type;
    private Double longitude;
    private Double latitude;
    private Integer peopleNumber;
    private Timestamp beginTime;
    private Timestamp deadline;
    private Collection<AuditDto> audits;

    public static TaskAppDto toDetail(Task task) {
        TaskStep taskStep = task.getTaskSteps().iterator().next();
        String headImg = taskStep.getImg() == null || taskStep.getImg().isEmpty() ? task.getUser().getHeadImg() : taskStep.getImg();

        TaskAppDto taskAppDto = new TaskAppDto();
        taskAppDto.setHeadImg(headImg);
        taskAppDto.setUsername(task.getUser().getUsername());

        ArrayList<AuditDto> auditDtos = new ArrayList<>();
        for (AuditTask auditTask : task.getAuditTasks()) {
            auditDtos.add(AuditDto.toDetail(auditTask.getAudit()));
        }
        taskAppDto.setAudits(auditDtos);
        BeanUtils.copyProperties(task, taskAppDto);
        return taskAppDto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getOriginalMoney() {
        return originalMoney;
    }

    public void setOriginalMoney(Float originalMoney) {
        this.originalMoney = originalMoney;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
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

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Collection<AuditDto> getAudits() {
        return audits;
    }

    public void setAudits(Collection<AuditDto> audits) {
        this.audits = audits;
    }
}
