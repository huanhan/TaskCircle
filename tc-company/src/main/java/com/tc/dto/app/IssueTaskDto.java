package com.tc.dto.app;

import com.tc.db.entity.Task;
import com.tc.db.enums.TaskType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 发布任务需要的DTO
 *
 * @author Cyg
 */
public class IssueTaskDto {

    @NotEmpty
    @Length(max = 32)
    private String id;

    @NotNull
    @Min(0)
    private Float money;

    @NotNull
    @Min(0)
    private Integer peopleNumber;

    @NotNull
    private Timestamp beginTime;

    @NotNull
    private Timestamp deadline;

    @NotNull
    @Min(0)
    private Integer permitAbandonMinute;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @NotNull
    private Boolean isTaskRework;

    @NotNull
    private Boolean isCompensate;

    @NotNull
    @Min(0)
    private Float compensateMoney;

    @NotEmpty
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Float getCompensateMoney() {
        return compensateMoney;
    }

    public void setCompensateMoney(Float compensateMoney) {
        this.compensateMoney = compensateMoney;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Task toTask(Task task, IssueTaskDto issueTask) {
        BeanUtils.copyProperties(issueTask, task);
        task.setOriginalMoney(issueTask.money);
        task.setType(issueTask.peopleNumber == 1 ? TaskType.SOLO : TaskType.MULTI);
        return task;
    }
}
