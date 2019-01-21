package com.tc.dto.user;

import com.tc.dto.enums.DateType;
import com.tc.until.TimestampHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Timestamp;

/**
 * 用户收支统计条件
 * @author Cyg
 */
public class DateCondition {

    public static final int MAX_HOURS = 24;
    public static final int MAX_DAY = 31;
    public static final int MAX_MONTH = 12;
    public static final int MAX_YEAR = 12;

    public static final String[] HOURS = {
            "00:00","01:00","02:00","03:00","04:00","05:00",
            "06:00","07:00","08:00","09:00","10:00","11:00",
            "12:00","13:00","14:00","15:00","16:00","17:00",
            "18:00","19:00","20:00","21:00","22:00","23:00"};

    @NotNull
    @Past
    private Timestamp begin;
    @NotNull
    @Past
    private Timestamp end;
    @NotNull
    private DateType type;

    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public DateType getType() {
        return type;
    }

    public void setType(DateType type) {
        this.type = type;
    }

    public static DateCondition reset(DateCondition condition){
        Timestamp begin = condition.getBegin();
        Timestamp end = condition.getEnd();
        if (begin.after(end)){
            begin = condition.getEnd();
            end = condition.getBegin();
        }
        long hours = 0;
        long day = 0;
        long mouth = 0;
        long year = 0;
        switch (condition.type){
            case HOURS:
                //判断是不是同一天
                if (TimestampHelper.isEqualDay(begin,end)){
                    //重置成某个小时的开始时间与结束时间
                    begin = TimestampHelper.toHouseBegin(begin);
                    end = TimestampHelper.toHouseEnd(end);
                }else {
                    hours = TimestampHelper.differByHours(begin,end);
                    if (hours > MAX_HOURS){
                        //如果超出了小时数可查看的最大范围
                        begin = TimestampHelper.toHouseBegin(begin);
                        end = TimestampHelper.addHours(begin,MAX_HOURS);
                    }else {
                        begin = TimestampHelper.toHouseBegin(begin);
                        end = TimestampHelper.toHouseEnd(end);
                    }
                }
                break;
            case DAY:
                hours = TimestampHelper.differByHours(begin,end);
                //判断天数是不是小于一天
                if (hours < MAX_HOURS){
                    //小于一天按小时统计
                    condition.setType(DateType.HOURS);
                    begin = TimestampHelper.toHouseBegin(begin);
                    end = TimestampHelper.toHouseEnd(end);
                }else {
                    //判断是不是同一个月
                    if (TimestampHelper.isEqualMonth(begin,end)){
                        begin = TimestampHelper.toDayBegin(begin);
                        end = TimestampHelper.toDayBegin(end);
                    }else {
                        //判断天数是不是大于30,最大只能查看30天的记录
                        day = TimestampHelper.differByDay(begin,end);
                        if (day > MAX_DAY){
                            begin = TimestampHelper.toDayBegin(begin);
                            end = TimestampHelper.addDay(begin,MAX_DAY);
                        }else {
                            begin = TimestampHelper.toDayBegin(begin);
                            end = TimestampHelper.toDayEnd(end);
                        }
                    }
                }
                break;
            case MONTH:
                //判断是不是同一年
                if (TimestampHelper.isEqualYear(begin,end)){
                    //判断是不是同一月
                    if (TimestampHelper.isEqualMonth(begin,end)){
                        //如果同一个月，按天统计
                        condition.setType(DateType.DAY);
                        begin = TimestampHelper.toDayBegin(begin);
                        end = TimestampHelper.toDayEnd(end);
                    }else {
                        //判断相差的天数
                        day = TimestampHelper.differByDay(begin,end);
                        if (day <= MAX_DAY){
                            //相差小于MAX_DAY按天统计
                            condition.setType(DateType.DAY);
                            begin = TimestampHelper.toDayBegin(begin);
                            end = TimestampHelper.toDayEnd(end);
                        }else {
                            begin = TimestampHelper.toMonthBegin(begin);
                            end = TimestampHelper.toMonthEnd(end);
                        }
                    }
                }else {
                    //判断相差的天数
                    day = TimestampHelper.differByDay(begin,end);
                    if (day <= MAX_DAY){
                        //相差小于MAX_DAY按天统计
                        condition.setType(DateType.DAY);
                        begin = TimestampHelper.toDayBegin(begin);
                        end = TimestampHelper.toDayEnd(end);
                    }else {
                        //判断相差的月数
                        mouth = TimestampHelper.differByMonth(begin,end);
                        if (mouth > MAX_MONTH){
                            begin = TimestampHelper.toMonthBegin(begin);
                            end = TimestampHelper.addMonth(begin,12);
                        }else {
                            begin = TimestampHelper.toMonthBegin(begin);
                            end = TimestampHelper.toMonthEnd(end);
                        }
                    }
                }
                break;
            case YEAR:
                //判断是不是同一年
                if (TimestampHelper.isEqualYear(begin,end)){
                    //判断是不是同一月
                    if (TimestampHelper.isEqualMonth(begin,end)){
                        //如果同一个月，按天统计
                        condition.setType(DateType.DAY);
                        begin = TimestampHelper.toDayBegin(begin);
                        end = TimestampHelper.toDayEnd(end);
                    }else {
                        //不是则按月统计
                        condition.setType(DateType.MONTH);
                        begin = TimestampHelper.toMonthBegin(begin);
                        end = TimestampHelper.toMonthEnd(end);
                    }
                }else {
                    year = TimestampHelper.differByYear(begin,end);
                    if (year > MAX_YEAR){
                        begin = TimestampHelper.toYearBegin(begin);
                        end = TimestampHelper.addYear(begin,MAX_YEAR);
                    }else {
                        //判断相差的月数
                        mouth = TimestampHelper.differByMonth(begin,end);
                        if (mouth > MAX_MONTH){
                            begin = TimestampHelper.toYearBegin(begin);
                            end = TimestampHelper.toYearEnd(end);
                        }else {
                            condition.setType(DateType.MONTH);
                            begin = TimestampHelper.toMonthBegin(begin);
                            end = TimestampHelper.toMonthEnd(end);
                        }

                    }
                }
                break;
            default:
                break;
        }
        condition.begin = begin;
        condition.end = end;
        return condition;
    }
}
