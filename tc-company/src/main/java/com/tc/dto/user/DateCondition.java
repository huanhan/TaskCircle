package com.tc.dto.user;

import com.tc.dto.enums.DateType;
import com.tc.until.TimestampHelper;

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

    private Timestamp begin;
    private Timestamp end;
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
