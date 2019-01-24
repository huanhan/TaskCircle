package com.tc.dto.user;


import com.tc.db.entity.Hunter;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.enums.HunterTaskState;
import com.tc.dto.enums.TaskConditionResult;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.statistics.TaskStatistics;
import com.tc.until.FloatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 猎刃接受的任务的统计
 * @author Cyg
 */
public class HunterTaskStatistics {
    private Hunter hunter;
    private long countAll;
    private float incomeAll;
    private float payAll;
    private float moneyAll;
    private List<TaskStatistics> statistics;

    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
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

    public float getIncomeAll() {
        return incomeAll;
    }

    public void setIncomeAll(float incomeAll) {
        this.incomeAll = incomeAll;
    }

    public float getPayAll() {
        return payAll;
    }

    public void setPayAll(float payAll) {
        this.payAll = payAll;
    }

    public static HunterTaskStatistics statistics(List<HunterTask> source, TaskCondition condition){
        HunterTaskStatistics result = new HunterTaskStatistics();
        List<String> items = condition.getItems();
        switch (condition.getSelect()){
            case STATE:
                List<HunterTaskState> states = HunterTaskState.byStr(items);
                result = statisticsByState(source,states);
                break;
            default:
                break;
        }
        statisticsOther(source,result);
        return result;
    }


    /**
     * 统计其他信息
     * @param statistics
     * @param source
     * @return
     */
    private static void statisticsOther(List<HunterTask> source,HunterTaskStatistics statistics){
        AtomicReference<Float> moneyAll = new AtomicReference<>(0F);
        AtomicReference<Float> payAll = new AtomicReference<>(0F);
        AtomicReference<Float> incomeAll = new AtomicReference<>(0F);
        source.forEach(task -> {
            moneyAll.updateAndGet(v -> FloatHelper.add(
                    v,
                    FloatHelper.isNull(task.getMoney()) ? task.getTask().getCompensateMoney() : task.getMoney()));
            if (task.getMoneyType() != null) {
                switch (task.getMoneyType()) {
                    case PAY:
                        payAll.updateAndGet(v -> FloatHelper.add(
                                v,
                                task.getMoney()
                        ));
                        break;
                    case INCOME:
                        incomeAll.updateAndGet(v -> FloatHelper.add(
                                v,
                                task.getMoney()
                        ));
                        break;
                    default:
                        break;
                }
            }
        });
        statistics.setMoneyAll(moneyAll.get());
        statistics.setIncomeAll(incomeAll.get());
        statistics.setPayAll(payAll.get());
        statistics.setCountAll(source.size());
    }

    private static HunterTaskStatistics statisticsByState(List<HunterTask> source, List<HunterTaskState> states) {
        HunterTaskStatistics result = new HunterTaskStatistics();
        List<TaskStatistics> statistics = new ArrayList<>();
        states.forEach(state -> {
            AtomicReference<Float> money = new AtomicReference<>(0f);
            AtomicLong count = new AtomicLong();
            source.forEach(task -> {
                if (task.getState().equals(state)){
                    money.updateAndGet(v -> FloatHelper.add(
                            v,
                            FloatHelper.isNull(task.getMoney()) ? task.getTask().getCompensateMoney() : task.getMoney())
                    );
                    count.getAndIncrement();
                }
            });
            statistics.add(new TaskStatistics(state.name(),money.get(),count.get(),state.getState()));
        });
        result.statistics = statistics;
        return result;
    }

    /**
     * 统计结果过滤的接口
     * @param hunterTaskStatistics 统计的结果
     * @param selectResult 需要过滤的选项
     * @return
     */
    public static HunterTaskStatistics filter(HunterTaskStatistics hunterTaskStatistics, TaskConditionResult selectResult){
        List<TaskStatistics> statistics = new ArrayList<>();
        switch (selectResult){
            case MONEY:
                hunterTaskStatistics.getStatistics().forEach(taskStatistics ->
                        statistics.add(
                                new TaskStatistics(
                                        taskStatistics.getKey(),
                                        taskStatistics.getMoney(),
                                        taskStatistics.getName()
                                )
                        )
                );
                hunterTaskStatistics.setStatistics(statistics);
                break;
            case NUMBER:
                hunterTaskStatistics.getStatistics().forEach(taskStatistics ->
                        statistics.add(
                                new TaskStatistics(
                                        taskStatistics.getKey(),
                                        taskStatistics.getCount(),
                                        taskStatistics.getName()
                                )
                        )
                );
                hunterTaskStatistics.setStatistics(statistics);
                break;
            default:
                break;
        }

        return hunterTaskStatistics;
    }
}
