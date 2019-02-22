package com.tc.dto.user;


import com.tc.db.entity.Hunter;
import com.tc.db.entity.HunterTask;
import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.enums.HunterTaskState;
import com.tc.db.enums.TaskState;
import com.tc.dto.enums.TaskConditionResult;
import com.tc.dto.statistics.TaskCondition;
import com.tc.dto.statistics.TaskStatistics;
import com.tc.dto.task.statistics.StatisticsCount;
import com.tc.dto.task.statistics.StatisticsMoney;
import com.tc.dto.trans.TransData;
import com.tc.dto.trans.TransEnum;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<TransData> trans;
    private List<TransEnum> items;

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


    public static HunterTaskStatistics statistics(List<HunterTask> source, TaskCondition condition){
        HunterTaskStatistics result = new HunterTaskStatistics();
        List<String> items = condition.getItems();
        switch (condition.getSelect()){
            case STATE:
                List<HunterTaskState> states = ListUtils.isNotEmpty(items) ? HunterTaskState.byStr(items) : Arrays.asList(HunterTaskState.values());
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
        result.trans = new ArrayList<>();
        result.items = new ArrayList<>();

        List<StatisticsCount> statisticsCounts = new ArrayList<>();
        List<StatisticsMoney> statisticsMonies = new ArrayList<>();
        List<TransEnum> items = new ArrayList<>();

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
            if (money.get() > 0 && count.get() > 0) {
                statisticsMonies.add(new StatisticsMoney(state.name(), state.getState(), money.get()));
                statisticsCounts.add(new StatisticsCount(state.name(), state.getState(), count.get()));
                items.add(new TransEnum(state.name(), state.getState()));
            }
        });

        result.trans.add(new TransData(TaskConditionResult.MONEY, TaskConditionResult.MONEY.getResult(), statisticsMonies));
        result.trans.add(new TransData(TaskConditionResult.NUMBER, TaskConditionResult.NUMBER.getResult(), statisticsCounts));
        result.setItems(items);
        return result;
    }

    /**
     * 统计结果过滤的接口
     * @param hunterTaskStatistics 统计的结果
     * @param selectResult 需要过滤的选项
     * @return
     */
    public static HunterTaskStatistics filter(HunterTaskStatistics hunterTaskStatistics, TaskConditionResult selectResult){
        List<TransData> result = new ArrayList<>();
        switch (selectResult){
            case MONEY:
                hunterTaskStatistics.trans.forEach(transData -> {
                    if (transData.getKey().equals(TaskConditionResult.MONEY)) {
                        result.add(transData);
                    }
                });
                break;
            case NUMBER:
                hunterTaskStatistics.trans.forEach(transData -> {
                    if (transData.getKey().equals(TaskConditionResult.NUMBER)) {
                        result.add(transData);
                    }
                });
                break;
            default:
                break;
        }
        return hunterTaskStatistics;
    }

    public HunterTaskStatistics append(Hunter hunter) {
        this.hunter = new Hunter(hunter.getUserId(),hunter.getUser());
        return this;
    }
}
