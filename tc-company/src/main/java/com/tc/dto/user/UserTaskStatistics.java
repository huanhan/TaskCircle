package com.tc.dto.user;

import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassify;
import com.tc.db.entity.User;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import com.tc.dto.enums.TaskConditionResult;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.statistics.TaskStatistics;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 用户任务统计
 * @author Cyg
 */
public class UserTaskStatistics {
    private User user;
    private long countAll;
    private float moneyAll;
    private List<TaskStatistics> statistics;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getCountAll() {
        return countAll;
    }

    public void setCountAll(long countAll) {
        this.countAll = countAll;
    }

    public float getMoneyAll() {
        return moneyAll;
    }

    public void setMoneyAll(float moneyAll) {
        this.moneyAll = moneyAll;
    }

    public List<TaskStatistics> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<TaskStatistics> statistics) {
        this.statistics = statistics;
    }

    /**
     * 统计的接口
     * @param tasks 元数据
     * @param condition 统计的条件
     * @param taskClassifies 按分类统计时需要的分类数据
     * @return
     */
    public static UserTaskStatistics statistics(List<Task> tasks, TaskCondition condition,List<TaskClassify> taskClassifies){
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        List<String> items = condition.getItems();
        switch (condition.getSelect()){
            case STATE:
                List<TaskState> states = TaskState.byStrs(items);
                userTaskStatistics = statisticsByState(tasks,states);
                break;
            case TYPE:
                List<TaskType> types = TaskType.byStrs(items);
                userTaskStatistics = statisticsByType(tasks,types);
                break;
            case CLASSIFY:
                userTaskStatistics = statisticsByClassify(tasks,taskClassifies);
                break;
            default:
                break;
        }

        userTaskStatistics.setCountAll(tasks.size());
        userTaskStatistics.setMoneyAll(moneyAll(tasks));
        return userTaskStatistics;
    }


    /**
     * 统计结果过滤的接口
     * @param userTaskStatistics 统计的结果
     * @param selectResult 需要过滤的选项
     * @return
     */
    public static UserTaskStatistics filter(UserTaskStatistics userTaskStatistics, TaskConditionResult selectResult){
        List<TaskStatistics> statistics = new ArrayList<>();
        switch (selectResult){
            case MONEY:
                userTaskStatistics.getStatistics().forEach(taskStatistics ->
                        statistics.add(
                                new TaskStatistics(
                                        taskStatistics.getKey(),
                                        taskStatistics.getMoney(),
                                        taskStatistics.getName()
                                )
                        )
                );
                userTaskStatistics.setStatistics(statistics);
                break;
            case NUMBER:
                userTaskStatistics.getStatistics().forEach(taskStatistics ->
                        statistics.add(
                                new TaskStatistics(
                                        taskStatistics.getKey(),
                                        taskStatistics.getCount(),
                                        taskStatistics.getName()
                                )
                        )
                );
                userTaskStatistics.setStatistics(statistics);
                break;
            default:
                break;
        }

        return userTaskStatistics;
    }

    /**
     * 统计总金额
     * @param tasks
     * @return
     */
    private static Float moneyAll(List<Task> tasks){
        AtomicReference<Float> money = new AtomicReference<>(0F);
        tasks.forEach(task -> money.updateAndGet(v -> FloatHelper.add(v, task.getOriginalMoney())));
        return money.get();
    }

    /**
     * 根据任务状态统计结果
     * @param tasks 元数据
     * @param states 需要的显示的状态
     * @return
     */
    private static UserTaskStatistics statisticsByState(List<Task> tasks, List<TaskState> states){
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        List<TaskStatistics> statistics = new ArrayList<>();
        states.forEach(taskState -> {
            AtomicReference<Float> money = new AtomicReference<>(0f);
            AtomicLong count = new AtomicLong();
            tasks.forEach(task -> {
                if (task.getState().equals(taskState)){
                    money.updateAndGet(v -> FloatHelper.add(v,task.getOriginalMoney()));
                    count.getAndIncrement();
                }
            });
            statistics.add(new TaskStatistics(taskState.name(),money.get(),count.get(),taskState.getState()));
        });
        userTaskStatistics.statistics = statistics;
        return userTaskStatistics;
    }


    /**
     * 根据任务类别统计结果
     * @param tasks 元数据
     * @param types 需要显示的类别
     * @return
     */
    private static UserTaskStatistics statisticsByType(List<Task> tasks, List<TaskType> types){
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        List<TaskStatistics> statistics = new ArrayList<>();
        types.forEach(taskType -> {
            AtomicReference<Float> money = new AtomicReference<>(0f);
            AtomicLong count = new AtomicLong();
            tasks.forEach(task -> {
                if (task.getType().equals(taskType)){
                    money.updateAndGet(v -> FloatHelper.add(v,task.getOriginalMoney()));
                    count.getAndIncrement();
                }
            });
            statistics.add(new TaskStatistics(taskType.name(),money.get(),count.get(),taskType.getType()));
        });
        userTaskStatistics.statistics = statistics;
        return userTaskStatistics;
    }


    /**
     * 根据任务分类统计结果
     * @param tasks 元数据
     * @param classify 需要显示的分类
     * @return
     */
    private static UserTaskStatistics statisticsByClassify(List<Task> tasks, List<TaskClassify> classify){
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        List<TaskStatistics> statistics = new ArrayList<>();
        classify.forEach(c -> {
            AtomicLong count = new AtomicLong();
            AtomicReference<Float> money = new AtomicReference<>(0f);
            tasks.forEach(task -> {
                if (!ListUtils.isEmpty(task.getTaskClassifyRelations())){
                    task.getTaskClassifyRelations().forEach(tcr -> {
                        if (tcr.getTaskClassifyId().equals(c.getId())){
                            count.getAndIncrement();
                            money.updateAndGet(v -> FloatHelper.add(v,task.getOriginalMoney()));
                        }
                    });
                }
            });
            statistics.add(new TaskStatistics(c.getId().toString(),money.get(),count.get(),c.getName()));
        });
        userTaskStatistics.statistics = statistics;
        return userTaskStatistics;
    }






}
