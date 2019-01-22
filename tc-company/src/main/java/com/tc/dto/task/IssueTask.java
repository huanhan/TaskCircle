package com.tc.dto.task;

import com.tc.db.entity.Task;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 发布任务需要的DTO
 * @author Cyg
 */
public class IssueTask {

    @NotEmpty
    @Length(max = 32)
    private String id;
    @NotNull
    @Min(0)
    private Float money;
    @NotNull
    @Min(0)
    private Float compensateMoney;
    @NotNull
    @Min(0)
    private Integer peopleNumber;
    @NotNull
    @Min(0)
    private Integer permitAbandonMinute;
    @NotNull
    private Boolean isTaskRework;
    @NotNull
    private Boolean isCompensate;



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

    public Float getCompensateMoney() {
        return compensateMoney;
    }

    public void setCompensateMoney(Float compensateMoney) {
        this.compensateMoney = compensateMoney;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public Integer getPermitAbandonMinute() {
        return permitAbandonMinute;
    }

    public void setPermitAbandonMinute(Integer permitAbandonMinute) {
        this.permitAbandonMinute = permitAbandonMinute;
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


    public static boolean toTask(Task task, IssueTask issueTask) {
        boolean isUpdate = false;
        if (!issueTask.isCompensate.equals(task.getCompensate())){
            task.setCompensate(issueTask.isCompensate);
            isUpdate = true;
        }
        if (!issueTask.isTaskRework.equals(task.getTaskRework())){
            task.setTaskRework(issueTask.isTaskRework);
            isUpdate = true;
        }
        if (!issueTask.compensateMoney.equals(task.getCompensateMoney())){
            task.setCompensateMoney(issueTask.compensateMoney);
            isUpdate = true;
        }
        if (!issueTask.money.equals(task.getMoney())){
            task.setCompensateMoney(issueTask.money);
            isUpdate = true;
        }
        if (!issueTask.peopleNumber.equals(task.getPeopleNumber())){
            task.setPeopleNumber(issueTask.peopleNumber);
            isUpdate = true;
        }
        if (!issueTask.permitAbandonMinute.equals(task.getPermitAbandonMinute())){
            task.setPermitAbandonMinute(issueTask.permitAbandonMinute);
            isUpdate = true;
        }
        task.setOriginalMoney(issueTask.money);
        return isUpdate;
    }
}
