package com.tc.dto.task;


import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassifyRelation;
import com.tc.db.entity.TaskStep;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import com.tc.until.IdGenerator;
import com.tc.until.ListUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加任务
 */
public class AddTask {

    @NotNull(message = "请设置发布任务用户")
    @Min(value = 1)
    private Long userId;
    @NotEmpty(message = "请设置任务名")
    @Length(max = 20,message = "任务名最大长度为20")
    private String name;
    @NotNull(message = "请设置赏金")
    @Min(value = 0,message = "赏金不能为负数")
    private Float money;
    @Min(value = 1,message = "可接人数最小为1")
    private Integer peopleNumber = 1;
    @NotEmpty(message = "请设置任务的描述信息")
    @Length(max = 255,message = "描述内容不得超过255个字")
    private String context;
    @NotNull
    @Future(message = "时间必须是未来的时间")
    private Timestamp beginTime;
    @Future(message = "时间必须是未来的时间")
    private Timestamp deadline;
    @Max(value = 10,message = "允许放弃的时间不应该超过10分钟")
    @Min(value = 3,message = "允许放弃的时间不应该低于3分钟")
    private Integer permitAbandonMinute = 5;
    @NotNull(message = "请设置任务的位置")
    private Double longitude;
    @NotNull(message = "请设置任务的位置")
    private Double latitude;
    private Boolean isTaskRework = Boolean.TRUE;
    private Boolean isCompensate = Boolean.TRUE;
    @NotEmpty(message = "请设置任务分类")
    private List<Long> classify;
    private List<AddTaskStep> taskSteps;

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

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public List<Long> getClassify() {
        return classify;
    }

    public void setClassify(List<Long> classify) {
        this.classify = classify;
    }

    public List<AddTaskStep> getTaskSteps() {
        return taskSteps;
    }

    public void setTaskSteps(List<AddTaskStep> taskSteps) {
        this.taskSteps = taskSteps;
    }

    public static Task toTask(AddTask addTask){
        Task task = new Task();
        task.setId(IdGenerator.INSTANCE.nextId());
        task.setUserId(addTask.userId);
        task.setName(addTask.name);
        task.setMoney(addTask.money);
        task.setPeopleNumber(addTask.peopleNumber);
        task.setState(TaskState.NEW_CREATE);
        task.setType(addTask.peopleNumber > 1 ? TaskType.MULTI : TaskType.SOLO);
        task.setContext(addTask.context);
        task.setBeginTime(addTask.beginTime);
        task.setDeadline(addTask.deadline);
        task.setPermitAbandonMinute(addTask.permitAbandonMinute);
        task.setLongitude(addTask.longitude);
        task.setLatitude(addTask.latitude);
        task.setTaskRework(addTask.isTaskRework);
        task.setCompensate(addTask.isCompensate);
        task.setTaskClassifyRelations(TaskClassifyRelation.toList(task.getId(),addTask.classify));
        task.setTaskSteps(TaskStep.toList(task.getId(),addTask.taskSteps));
        return task;
    }
}
