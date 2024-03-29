package com.tc.dto.user;

import com.tc.db.entity.User;
import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.WithdrawType;
import com.tc.dto.enums.DateType;
import com.tc.dto.enums.IEType;
import com.tc.dto.statistics.Statistics;
import com.tc.dto.task.statistics.StatisticsMoney;
import com.tc.dto.task.statistics.StatisticsMoneyAndTime;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransData;
import com.tc.dto.trans.TransEnum;
import com.tc.until.FloatHelper;
import com.tc.until.ListUtils;
import com.tc.until.TimestampHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用户收支统计
 * @author Cyg
 */
public class UserWithdrawStatistics {

    /**
     * 用户基本信息
     */
    private User user;
    /**
     * 支出总额
     */
    private Float expendAll;
    /**
     * 收入总额
     */
    private Float incomeAll;
    /**
     * 标题
     */
    private String title;
    /**
     * 统计的类别
     */
    private Trans type;

    private List<TransData> trans;
    private List<Timestamp> items;



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Float getExpendAll() {
        return expendAll;
    }

    public void setExpendAll(Float expendAll) {
        this.expendAll = expendAll;
    }

    public Float getIncomeAll() {
        return incomeAll;
    }

    public void setIncomeAll(Float incomeAll) {
        this.incomeAll = incomeAll;
    }

    public Trans getType() {
        return type;
    }

    public void setType(Trans type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<TransData> getTrans() {
        return trans;
    }

    public void setTrans(List<TransData> trans) {
        this.trans = trans;
    }

    public List<Timestamp> getItems() {
        return items;
    }

    public void setItems(List<Timestamp> items) {
        this.items = items;
    }

    public static UserWithdrawStatistics statistics(List<UserWithdraw> userWithdraws, DateType type){
        UserWithdrawStatistics userWithdrawStatistics = new UserWithdrawStatistics();
        if (!ListUtils.isEmpty(userWithdraws)){
            switch (type){
                case HOURS:
                    userWithdrawStatistics = statisticsByHours(userWithdraws);
                    break;
                case DAY:
                    userWithdrawStatistics = statisticsByDay(userWithdraws);
                    break;
                case MONTH:
                    userWithdrawStatistics = statisticsByMonth(userWithdraws);
                    break;
                case YEAR:
                    userWithdrawStatistics = statisticsByYear(userWithdraws);
                    break;
                default:
                    break;
            }
        }
        userWithdrawStatistics.setType(new Trans(type.name(),type.getType()));
        return userWithdrawStatistics;
    }



    /**
     * 根据具体的小时统计结果
     * @param userWithdraws
     * @return
     */
    private static UserWithdrawStatistics statisticsByHours(List<UserWithdraw> userWithdraws){
        UserWithdrawStatistics userWithdrawStatistics = new UserWithdrawStatistics();
        userWithdrawStatistics.trans = new ArrayList<>();
        //初始化统计内容
        Float expendAll = 0F;
        Float incomeAll = 0F;
        List<StatisticsMoneyAndTime> expends = new ArrayList<>();
        List<StatisticsMoneyAndTime> incomes = new ArrayList<>();

        //遍历数据与统计
        for (UserWithdraw withdraw : userWithdraws) {
            //充值即支出
            if (withdraw.getType().equals(WithdrawType.PAY)){
                expendAll = FloatHelper.add(expendAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                expends.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toHouseBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    Timestamp houseBegin = TimestampHelper.toHouseBegin(withdraw.getAuditPassTime());
                    expends.add(new StatisticsMoneyAndTime(houseBegin,withdraw.getMoney()));
                }
            }else if (withdraw.getType().equals(WithdrawType.WITHDRAW)){
                incomeAll = FloatHelper.add(incomeAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                incomes.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toHouseBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    Timestamp houseBegin = TimestampHelper.toHouseBegin(withdraw.getAuditPassTime());
                    incomes.add(new StatisticsMoneyAndTime(houseBegin,withdraw.getMoney()));
                }
            }
        }




        //设置统计标题（一般按时间）
        userWithdrawStatistics.setTitle(TimestampHelper.toDay(userWithdraws.get(0).getAuditPassTime()));
        userWithdrawStatistics.setExpendAll(expendAll);
        userWithdrawStatistics.setIncomeAll(incomeAll);
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.PAY,WithdrawType.PAY.getType(),expends));
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.WITHDRAW,WithdrawType.WITHDRAW.getType(),incomes));
        userWithdrawStatistics.items = createTimestamps(expends,incomes);
        return userWithdrawStatistics;
    }

    /**
     * 按日统计
     * @param userWithdraws
     * @return
     */
    private static UserWithdrawStatistics statisticsByDay(List<UserWithdraw> userWithdraws){
        UserWithdrawStatistics userWithdrawStatistics = new UserWithdrawStatistics();
        userWithdrawStatistics.trans = new ArrayList<>();
        //初始化统计内容
        Float expendAll = 0F;
        Float incomeAll = 0F;
        List<StatisticsMoneyAndTime> expends = new ArrayList<>();
        List<StatisticsMoneyAndTime> incomes = new ArrayList<>();

        //遍历数据与统计
        for (UserWithdraw withdraw : userWithdraws) {
            //充值即支出
            if (withdraw.getType().equals(WithdrawType.PAY)){
                expendAll = FloatHelper.add(expendAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                expends.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toDayBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    Timestamp dayBegin = TimestampHelper.toDayBegin(withdraw.getAuditPassTime());
                    expends.add(new StatisticsMoneyAndTime(dayBegin,withdraw.getMoney()));
                }
            }else if (withdraw.getType().equals(WithdrawType.WITHDRAW)){
                incomeAll = FloatHelper.add(incomeAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                incomes.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toDayBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    Timestamp dayBegin = TimestampHelper.toDayBegin(withdraw.getAuditPassTime());
                    incomes.add(new StatisticsMoneyAndTime(dayBegin,withdraw.getMoney()));
                }
            }
        }

        //设置统计标题（一般按时间）
        userWithdrawStatistics.setTitle(TimestampHelper.toMonth(userWithdraws.get(0).getAuditPassTime()));
        userWithdrawStatistics.setExpendAll(expendAll);
        userWithdrawStatistics.setIncomeAll(incomeAll);
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.PAY,WithdrawType.PAY.getType(),expends));
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.WITHDRAW,WithdrawType.WITHDRAW.getType(),incomes));
        userWithdrawStatistics.items = createTimestamps(expends,incomes);
        return userWithdrawStatistics;
    }

    /**
     * 按月统计
     * @param userWithdraws
     * @return
     */
    private static UserWithdrawStatistics statisticsByMonth(List<UserWithdraw> userWithdraws){
        UserWithdrawStatistics userWithdrawStatistics = new UserWithdrawStatistics();
        userWithdrawStatistics.trans = new ArrayList<>();

        //初始化统计内容
        Float expendAll = 0F;
        Float incomeAll = 0F;
        List<StatisticsMoneyAndTime> expends = new ArrayList<>();
        List<StatisticsMoneyAndTime> incomes = new ArrayList<>();

        //遍历数据与统计
        for (UserWithdraw withdraw : userWithdraws) {
            //充值即支出
            if (withdraw.getType().equals(WithdrawType.PAY)){
                expendAll = FloatHelper.add(expendAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                expends.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toMonthBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    expends.add(new StatisticsMoneyAndTime(TimestampHelper.toMonthBegin(withdraw.getAuditPassTime()),withdraw.getMoney()));
                }
            }else if (withdraw.getType().equals(WithdrawType.WITHDRAW)){
                incomeAll = FloatHelper.add(incomeAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                incomes.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toMonthBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    incomes.add(new StatisticsMoneyAndTime(TimestampHelper.toMonthBegin(withdraw.getAuditPassTime()),withdraw.getMoney()));
                }
            }
        }

        //设置统计标题（一般按时间）
        userWithdrawStatistics.setTitle(TimestampHelper.toYear(userWithdraws.get(0).getAuditPassTime()));
        userWithdrawStatistics.setExpendAll(expendAll);
        userWithdrawStatistics.setIncomeAll(incomeAll);
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.PAY,WithdrawType.PAY.getType(),expends));
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.WITHDRAW,WithdrawType.WITHDRAW.getType(),incomes));
        userWithdrawStatistics.items = createTimestamps(expends,incomes);
        return userWithdrawStatistics;
    }

    /**
     * 按年统计
     * @param userWithdraws
     * @return
     */
    private static UserWithdrawStatistics statisticsByYear(List<UserWithdraw> userWithdraws){
        UserWithdrawStatistics userWithdrawStatistics = new UserWithdrawStatistics();
        userWithdrawStatistics.trans = new ArrayList<>();

        //初始化统计内容
        Float expendAll = 0F;
        Float incomeAll = 0F;
        List<StatisticsMoneyAndTime> expends = new ArrayList<>();
        List<StatisticsMoneyAndTime> incomes = new ArrayList<>();

        //遍历数据与统计
        for (UserWithdraw withdraw : userWithdraws) {
            //充值即支出
            if (withdraw.getType().equals(WithdrawType.PAY)){
                expendAll = FloatHelper.add(expendAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                expends.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toYearBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    expends.add(new StatisticsMoneyAndTime(TimestampHelper.toYearBegin(withdraw.getAuditPassTime()),withdraw.getMoney()));
                }
            }else if (withdraw.getType().equals(WithdrawType.WITHDRAW)){
                incomeAll = FloatHelper.add(incomeAll,withdraw.getMoney());
                AtomicBoolean isSelect = new AtomicBoolean(false);
                incomes.forEach(statistics -> {
                    if (statistics.getKey().equals(TimestampHelper.toYearBegin(withdraw.getAuditPassTime()))){
                        statistics.setMoney(FloatHelper.add(statistics.getMoney(),withdraw.getMoney()));
                        isSelect.set(true);
                    }
                });
                if (!isSelect.get()){
                    incomes.add(new StatisticsMoneyAndTime(TimestampHelper.toYearBegin(withdraw.getAuditPassTime()),withdraw.getMoney()));
                }
            }
        }


        Timestamp[] ts1 = findMaxAndMin(expends);
        Timestamp[] ts2 = findMaxAndMin(incomes);
        Timestamp min = ts1[0] == null ? ts2[0] : (ts1[0].before(ts2[0]) ? ts1[0] : ts2[0]);
        Timestamp max = ts1[1] == null ? ts2[1] : (ts1[1].after(ts2[1]) ? ts1[1] : ts2[1]);
        String title = min.toLocalDateTime().getYear() + "-" + max.toLocalDateTime().getYear();

        //设置统计标题（一般按时间）
        userWithdrawStatistics.setTitle(title);
        userWithdrawStatistics.setExpendAll(expendAll);
        userWithdrawStatistics.setIncomeAll(incomeAll);
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.PAY,WithdrawType.PAY.getType(),expends));
        userWithdrawStatistics.trans.add(new TransData(WithdrawType.WITHDRAW,WithdrawType.WITHDRAW.getType(),incomes));
        userWithdrawStatistics.items = createTimestamps(expends,incomes);
        return userWithdrawStatistics;
    }



    private static Timestamp[] findMaxAndMin(List<StatisticsMoneyAndTime> statistics){
        Timestamp[] t = new Timestamp[2];

        if (!ListUtils.isEmpty(statistics)){
            Timestamp min = statistics.get(0).getKey();
            Timestamp max = statistics.get(0).getKey();
            for (StatisticsMoneyAndTime statistic : statistics) {
                if (statistic.getKey().after(max)){
                    max = statistic.getKey();
                }else if (statistic.getKey().before(min)){
                    min = statistic.getKey();
                }
            }
            t[0] = min;
            t[1] = max;
        }

        return t;

    }


    private static List<Timestamp> createTimestamps(List<StatisticsMoneyAndTime> tExpends,List<StatisticsMoneyAndTime> tIncomes){
        List<Timestamp> result = new ArrayList<>();
        List<StatisticsMoneyAndTime> expends = new ArrayList<>(tExpends);
        List<StatisticsMoneyAndTime> incomes = new ArrayList<>(tIncomes);
        List<StatisticsMoneyAndTime> center = new ArrayList<>();
        expends.removeIf(statisticsMoneyAndTime -> {
            boolean isDel = false;
            for (StatisticsMoneyAndTime tIncome : tIncomes) {
                isDel = statisticsMoneyAndTime.getKey().equals(tIncome.getKey());
                if (isDel){
                    center.add(statisticsMoneyAndTime);
                    break;
                }
            }
            return isDel;
        });
        incomes.removeIf(statisticsMoneyAndTime -> {
            boolean isDel = false;
            for (StatisticsMoneyAndTime tExpend : tExpends) {
                isDel = statisticsMoneyAndTime.getKey().equals(tExpend.getKey());
                if (isDel){
                    break;
                }
            }
            return isDel;
        });
        expends.forEach(statisticsMoneyAndTime -> result.add(statisticsMoneyAndTime.getKey()));
        incomes.forEach(statisticsMoneyAndTime -> result.add(statisticsMoneyAndTime.getKey()));
        center.forEach(statisticsMoneyAndTime -> result.add(statisticsMoneyAndTime.getKey()));
        result.sort((o1, o2) -> (int) (o1.getTime() - o2.getTime()));
        return result;
    }

}
