package com.tc.dto.user;

import com.tc.db.entity.Task;
import com.tc.db.entity.TaskClassify;
import com.tc.db.entity.User;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.db.enums.TaskType;
import com.tc.dto.enums.TaskConditionResult;
import com.tc.dto.enums.TaskConditionSelect;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.statistics.TaskStatistics;
import com.tc.dto.task.statistics.StatisticsCount;
import com.tc.dto.task.statistics.StatisticsMoney;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransData;
import com.tc.dto.trans.TransEnum;
import com.tc.dto.trans.TransTaskConditionQuery;
import com.tc.exception.DBException;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 用户任务统计
 *
 * @author Cyg
 */
public class UserTaskStatistics {
    private User user;
    private long countAll;
    private float moneyAll;
    private List<TransData> trans;
    private List<TransEnum> items;

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


    public List<TransData> getTrans() {
        return trans;
    }

    public void setTrans(List<TransData> trans) {
        this.trans = trans;
    }

    public List<TransEnum> getItems() {
        return items;
    }

    public void setItems(List<TransEnum> items) {
        this.items = items;
    }

    /**
     * 统计的接口
     *
     * @param tasks          元数据
     * @param condition      统计的条件
     * @param taskClassifies 按分类统计时需要的分类数据
     * @return
     */
    public static UserTaskStatistics statistics(List<Task> tasks, TaskCondition condition, List<TaskClassify> taskClassifies) {
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        List<String> items = condition.getItems();
        switch (condition.getSelect()) {
            case STATE:
                List<TaskState> states = ListUtils.isNotEmpty(items) ? TaskState.byStrs(items) : Arrays.asList(TaskState.values());
                userTaskStatistics = statisticsByState(tasks, states);
                break;
            case TYPE:
                List<TaskType> types = ListUtils.isNotEmpty(items) ? TaskType.byStrs(items) : Arrays.asList(TaskType.values());
                userTaskStatistics = statisticsByType(tasks, types);
                break;
            case CLASSIFY:
                userTaskStatistics = statisticsByClassify(tasks, taskClassifies);
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
     *
     * @param userTaskStatistics 统计的结果
     * @param selectResult       需要过滤的选项
     * @return
     */
    public static UserTaskStatistics filter(UserTaskStatistics userTaskStatistics, TaskConditionResult selectResult) {
        List<TransData> result = new ArrayList<>();

        switch (selectResult) {
            case MONEY:
                userTaskStatistics.trans.forEach(transData -> {
                    if (transData.getKey().equals(TaskConditionResult.MONEY)) {
                        result.add(transData);
                    }
                });
                break;
            case NUMBER:
                userTaskStatistics.trans.forEach(transData -> {
                    if (transData.getKey().equals(TaskConditionResult.NUMBER)) {
                        result.add(transData);
                    }
                });
                break;
            default:
                break;
        }

        if (ListUtils.isNotEmpty(result)) {
            userTaskStatistics.trans = result;
        }

        return userTaskStatistics;
    }

    /**
     * 统计总金额
     *
     * @param tasks
     * @return
     */
    private static Float moneyAll(List<Task> tasks) {
        AtomicReference<Float> money = new AtomicReference<>(0F);
        tasks.forEach(task -> money.updateAndGet(v -> FloatHelper.add(v, task.getOriginalMoney())));
        return money.get();
    }

    /**
     * 根据任务状态统计结果
     *
     * @param tasks  元数据
     * @param states 需要的显示的状态
     * @return
     */
    private static UserTaskStatistics statisticsByState(List<Task> tasks, List<TaskState> states) {
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        userTaskStatistics.trans = new ArrayList<>();

        List<StatisticsCount> statisticsCounts = new ArrayList<>();
        List<StatisticsMoney> statisticsMonies = new ArrayList<>();
        List<TransEnum> items = new ArrayList<>();

        states.forEach(taskState -> {
            AtomicReference<Float> money = new AtomicReference<>(0f);
            AtomicLong count = new AtomicLong();
            tasks.forEach(task -> {
                if (task.getState().equals(taskState)) {
                    money.updateAndGet(v -> FloatHelper.add(v, task.getOriginalMoney()));
                    count.getAndIncrement();
                }
            });
            if (money.get() > 0 && count.get() > 0) {
                statisticsMonies.add(new StatisticsMoney(taskState.name(), taskState.getState(), money.get()));
                statisticsCounts.add(new StatisticsCount(taskState.name(), taskState.getState(), count.get()));
                items.add(new TransEnum(taskState.name(), taskState.getState()));
            }

        });

        userTaskStatistics.trans.add(new TransData(TaskConditionResult.MONEY, TaskConditionResult.MONEY.getResult(), statisticsMonies));
        userTaskStatistics.trans.add(new TransData(TaskConditionResult.NUMBER, TaskConditionResult.NUMBER.getResult(), statisticsCounts));
        userTaskStatistics.setItems(items);

        return userTaskStatistics;
    }


    /**
     * 根据任务类别统计结果
     *
     * @param tasks 元数据
     * @param types 需要显示的类别
     * @return
     */
    private static UserTaskStatistics statisticsByType(List<Task> tasks, List<TaskType> types) {
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        userTaskStatistics.trans = new ArrayList<>();
        List<StatisticsCount> statisticsCounts = new ArrayList<>();
        List<StatisticsMoney> statisticsMonies = new ArrayList<>();
        List<TransEnum> items = new ArrayList<>();
        types.forEach(taskType -> {
            AtomicReference<Float> money = new AtomicReference<>(0f);
            AtomicLong count = new AtomicLong();
            tasks.forEach(task -> {
                if (task.getType().equals(taskType)) {
                    money.updateAndGet(v -> FloatHelper.add(v, task.getOriginalMoney()));
                    count.getAndIncrement();
                }
            });
            if (money.get() > 0 && count.get() > 0) {
                statisticsCounts.add(new StatisticsCount(taskType.name(), taskType.getType(), count.get()));
                statisticsMonies.add(new StatisticsMoney(taskType.name(), taskType.getType(), money.get()));
                items.add(new TransEnum(taskType.name(), taskType.getType()));
            }
        });
        userTaskStatistics.trans.add(new TransData(TaskConditionResult.MONEY, TaskConditionResult.MONEY.getResult(), statisticsMonies));
        userTaskStatistics.trans.add(new TransData(TaskConditionResult.NUMBER, TaskConditionResult.NUMBER.getResult(), statisticsCounts));
        userTaskStatistics.setItems(items);
        return userTaskStatistics;
    }


    /**
     * 根据任务分类统计结果
     *
     * @param tasks    元数据
     * @param classify 需要显示的分类
     * @return
     */
    private static UserTaskStatistics statisticsByClassify(List<Task> tasks, List<TaskClassify> classify) {
        UserTaskStatistics userTaskStatistics = new UserTaskStatistics();
        userTaskStatistics.trans = new ArrayList<>();
        List<StatisticsCount> statisticsCounts = new ArrayList<>();
        List<StatisticsMoney> statisticsMonies = new ArrayList<>();
        List<TransEnum> items = new ArrayList<>();
        classify.forEach(c -> {
            AtomicLong count = new AtomicLong();
            AtomicReference<Float> money = new AtomicReference<>(0f);
            tasks.forEach(task -> {
                if (!ListUtils.isEmpty(task.getTaskClassifyRelations())) {
                    task.getTaskClassifyRelations().forEach(tcr -> {
                        if (tcr.getTaskClassifyId().equals(c.getId())) {
                            count.getAndIncrement();
                            money.updateAndGet(v -> FloatHelper.add(v, task.getOriginalMoney()));
                        }
                    });
                }
            });
            if (money.get() > 0 && count.get() > 0) {
                statisticsCounts.add(new StatisticsCount(c.getId().toString(), c.getName(), count.get()));
                statisticsMonies.add(new StatisticsMoney(c.getId().toString(), c.getName(), money.get()));
                items.add(new TransEnum(c.getId().toString(), c.getName()));
            }
        });
        userTaskStatistics.trans.add(new TransData(TaskConditionResult.MONEY, TaskConditionResult.MONEY.getResult(), statisticsMonies));
        userTaskStatistics.trans.add(new TransData(TaskConditionResult.NUMBER, TaskConditionResult.NUMBER.getResult(), statisticsCounts));
        userTaskStatistics.setItems(items);
        return userTaskStatistics;
    }

    public UserTaskStatistics appand(User user) {
        this.user = new User(user.getId(), user.getName(), user.getUsername());
        return this;
    }
}
