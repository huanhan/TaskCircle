package com.tc.dto.user;

import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.entity.UserWithdraw;
import com.tc.until.ListUtils;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户押金信息
 * @author Cyg
 */
public class CashPledge {
    private String id;
    private Long userId;
    private User user;
    private Float money;
    private Timestamp createTime;
    private String name;

    public CashPledge() {
    }

    public CashPledge(String id, Long userId, User user, Float money, Timestamp createTime, String name) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.money = money;
        this.createTime = createTime;
        this.name = name;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<CashPledge> create(List<Task> tasks, List<UserWithdraw> userWithdraws, List<HunterTask> hunterTasks) {
        List<CashPledge> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(tasks)){
            tasks.forEach(task -> result.add(new CashPledge(
                    task.getId(),
                    task.getUserId(),
                    task.getUser(),
                    task.getMoney(),
                    task.getIssueTime(),
                    "任务"
            )));
        }
        if (ListUtils.isNotEmpty(userWithdraws)){
            userWithdraws.forEach(userWithdraw -> result.add(new CashPledge(
                    userWithdraw.getId(),
                    userWithdraw.getUserId(),
                    userWithdraw.getUser(),
                    userWithdraw.getMoney(),
                    userWithdraw.getCreateTime(),
                    "提现审核"
            )));
        }
        if (ListUtils.isNotEmpty(hunterTasks)){
            hunterTasks.forEach(hunterTask -> result.add(new CashPledge(
                    hunterTask.getId(),
                    hunterTask.getHunterId(),
                    hunterTask.getHunter().getUser(),
                    hunterTask.getTask().getCompensateMoney(),
                    hunterTask.getAcceptTime(),
                    "猎刃任务"
            )));
        }
        return result;
    }

    public static List<CashPledge> toListInIndex(List<CashPledge> cashPledges) {
        if (ListUtils.isNotEmpty(cashPledges)){
            cashPledges.forEach(cashPledge -> {
                if (cashPledge.user != null){
                    User user = cashPledge.user;
                    cashPledge.user = new User(user.getId(),user.getName(),user.getUsername());
                }
            });
        }
        return cashPledges;
    }

}
