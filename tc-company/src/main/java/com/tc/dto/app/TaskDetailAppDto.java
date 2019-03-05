package com.tc.dto.app;

import com.tc.db.entity.*;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import com.tc.dto.task.AddTaskStep;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskDetailAppDto {

    private String id;
    private Long userId;
    private String name;
    private String context;
    private String username;
    private String headImg;
    private Float money;
    private TaskState state;
    private TaskType type;
    private Integer peopleNumber;
    private Timestamp createTime;
    private Timestamp beginTime;
    private Timestamp deadline;
    private Timestamp auditTime;
    private Timestamp adminAuditTime;
    private Timestamp issueTime;
    private Float originalMoney;
    private Float compensateMoney;
    private Integer permitAbandonMinute;
    private Double longitude;
    private Double latitude;
    private String address;
    private Boolean isTaskRework;
    private Boolean isCompensate;
    private Boolean pick;

    private Collection<TaskClassifyAppDto> taskClassifyAppDtos;
    private Collection<AddTaskStep> taskSteps;
    private Collection<AuditDto> audits;

    public static TaskDetailAppDto toDetail(Task task) {
        TaskDetailAppDto taskDetailAppDto = new TaskDetailAppDto();
        BeanUtils.copyProperties(task, taskDetailAppDto);
        if (task.getUser() != null) {
            taskDetailAppDto.setHeadImg(task.getUser().getHeadImg());
            taskDetailAppDto.setUsername(task.getUser().getUsername());
        }

        List<TaskClassifyAppDto> taskClassifyAppDtos = new ArrayList<>();
        TaskClassifyAppDto childTaskClassifyAppDto;
        //循环增加子
        for (TaskClassifyRelation taskClassifyRelation : task.getTaskClassifyRelations()) {
            TaskClassify taskClassify = taskClassifyRelation.getTaskClassify();
            if (taskClassify != null) {
                childTaskClassifyAppDto = new TaskClassifyAppDto();
                BeanUtils.copyProperties(taskClassify, childTaskClassifyAppDto);
                taskClassifyAppDtos.add(childTaskClassifyAppDto);
            }
        }
        List<AddTaskStep> taskSteps = new ArrayList<>();
        AddTaskStep addTaskStep;
        for (TaskStep taskStep : task.getTaskSteps()) {
            addTaskStep = new AddTaskStep();
            BeanUtils.copyProperties(taskStep, addTaskStep);
            taskSteps.add(addTaskStep);
        }
        taskDetailAppDto.setTaskClassifyAppDtos(taskClassifyAppDtos);
        taskDetailAppDto.setTaskSteps(taskSteps);

        ArrayList<AuditDto> auditDtos = new ArrayList<>();
        for (AuditTask auditTask : task.getAuditTasks()) {
            auditDtos.add(AuditDto.toDetail(auditTask.getAudit()));
        }
        taskDetailAppDto.setAudits(auditDtos);

        return taskDetailAppDto;
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

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public Timestamp getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime) {
        this.auditTime = auditTime;
    }

    public Timestamp getAdminAuditTime() {
        return adminAuditTime;
    }

    public void setAdminAuditTime(Timestamp adminAuditTime) {
        this.adminAuditTime = adminAuditTime;
    }

    public Timestamp getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Timestamp issueTime) {
        this.issueTime = issueTime;
    }

    public Float getOriginalMoney() {
        return originalMoney;
    }

    public void setOriginalMoney(Float originalMoney) {
        this.originalMoney = originalMoney;
    }

    public Float getCompensateMoney() {
        return compensateMoney;
    }

    public void setCompensateMoney(Float compensateMoney) {
        this.compensateMoney = compensateMoney;
    }

    public Integer getPermitAbandonMinute() {
        return permitAbandonMinute;
    }

    public void setPermitAbandonMinute(Integer permitAbandonMinute) {
        this.permitAbandonMinute = permitAbandonMinute;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getTaskRework() {
        return isTaskRework;
    }

    public void setTaskRework(Boolean taskRework) {
        isTaskRework = taskRework;
    }

    public Boolean getCompensate() {
        return isCompensate;
    }

    public void setCompensate(Boolean compensate) {
        isCompensate = compensate;
    }

    public Collection<TaskClassifyAppDto> getTaskClassifyAppDtos() {
        return taskClassifyAppDtos;
    }

    public void setTaskClassifyAppDtos(Collection<TaskClassifyAppDto> taskClassifyAppDtos) {
        this.taskClassifyAppDtos = taskClassifyAppDtos;
    }

    public Collection<AddTaskStep> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(Collection<AddTaskStep> taskSteps) {
        this.taskSteps = taskSteps;
    }

    public Boolean getPick() {
        return pick;
    }

    public void setPick(Boolean pick) {
        this.pick = pick;
    }

    public Collection<AuditDto> getAudits() {
        return audits;
    }

    public void setAudits(Collection<AuditDto> audits) {
        this.audits = audits;
    }
}
