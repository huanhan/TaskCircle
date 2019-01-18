package com.tc.until;


import com.tc.db.enums.DateType;

import java.sql.Timestamp;
import java.time.*;
import java.util.Date;

/**
 * TimestampHelper工具
 * @author Cyg
 */
public class TimestampHelper {


    /**
     * 获取当前时间
     * @return
     */
    public static Timestamp today(){
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 判断传入的时间是否当天
     * @param timestamp
     * @return
     */
    public static boolean isToday(Timestamp timestamp){
        LocalDate today = todayBegin();
        LocalDate current = toLocalDate(timestamp);
        return current.isEqual(today);
    }

    /**
     * 某一天的开始时间
     * @param timestamp
     * @return
     */
    public static Timestamp toDayBegin(Timestamp timestamp){
        LocalDate day = toLocalDate(timestamp);
        return toTimestamp(day);
    }

    /**
     * 某一天的结束时间
     * @param timestamp
     * @return
     */
    public static Timestamp toDayEnd(Timestamp timestamp){
        LocalDate day = toLocalDate(timestamp);
        LocalDateTime localDateTime = LocalDateTime.of(day.getYear(),day.getMonth(),day.getDayOfMonth(),23,59,59);
        return toTimestamp(localDateTime);
    }

    /**
     * 某一月的开始时间
     * @param timestamp
     * @return
     */
    public static Timestamp toMonthBegin(Timestamp timestamp){
        LocalDate day = toLocalDate(timestamp);
        LocalDate current = LocalDate.of(day.getYear(),day.getMonth(),1);
        return toTimestamp(current);
    }

    /**
     * 某一月的结束时间
     * @param timestamp
     * @return
     */
    public static Timestamp toMonthEnd(Timestamp timestamp){
        LocalDate day = toLocalDate(timestamp);
        LocalDateTime localDateTime = LocalDateTime.of(day.getYear(),day.getMonth(),day.lengthOfMonth(),23,59,59);
        return toTimestamp(localDateTime);
    }

    /**
     * 某一年的开始时间
     * @param timestamp
     * @return
     */
    public static Timestamp toYearBegin(Timestamp timestamp){
        LocalDate day = toLocalDate(timestamp);
        LocalDate current = LocalDate.of(day.getYear(),1,1);
        return toTimestamp(current);
    }

    /**
     * 某一年的结束时间
     * @param timestamp
     * @return
     */
    public static Timestamp toYearEnd(Timestamp timestamp){
        LocalDate day = toLocalDate(timestamp);
        LocalDateTime localDateTime = LocalDateTime.of(day.getYear(), Month.DECEMBER,31,23,59,59);
        return toTimestamp(localDateTime);
    }

    /**
     * 获取当日开始的时间
     * @return
     */
    public static LocalDate todayBegin(){
        Timestamp today = new Timestamp(System.currentTimeMillis());
        LocalDateTime todayByLDT = today.toLocalDateTime();
        return LocalDate.of(todayByLDT.getYear(),todayByLDT.getMonth(),todayByLDT.getDayOfMonth());
    }

    /**
     * 获取当日结束的时间
     * @return
     */
    public static LocalDateTime todayEnd(){
        Timestamp today = new Timestamp(System.currentTimeMillis());
        LocalDateTime todayByLDT = today.toLocalDateTime();
        return LocalDateTime.of(todayByLDT.getYear(),todayByLDT.getMonth(),todayByLDT.getDayOfMonth(),23,59,59);
    }

    /**
     * 获取当日开始的时间
     * @return
     */
    public static Timestamp todayBeginByTimestamp(){
        return toTimestamp(todayBegin());
    }


    /**
     * 获取当日结束的时间
     * @return
     */
    public static Timestamp todayEndByTimestamp(){
        return toTimestamp(todayEnd());
    }

    /**
     * 将localDate转成Timestamp
     * @param localDate
     * @return
     */
    public static Timestamp toTimestamp(LocalDate localDate){
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return new Timestamp(Date.from(instant).getTime());
    }

    /**
     * 将localDateTime转成Timestamp
     * @param localDateTime
     * @return
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime){
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return new Timestamp(Date.from(instant).getTime());
    }

    /**
     * 将timestamp转成LocalDate
     * @param timestamp
     * @return
     */
    public static LocalDate toLocalDate(Timestamp timestamp){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return LocalDate.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth());
    }

    /**
     * 将timestamp转成月份
     * 格式 2019年11月
     * @param timestamp
     * @return
     */
    public static String toMonth(Timestamp timestamp){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.getYear() + "年" + localDateTime.getMonth().getValue() + "月";
    }

    /**
     * 将timestamp转成月份
     * 格式 2019年11月
     * @param timestamp
     * @return
     */
    public static String toYear(Timestamp timestamp){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.getYear() + "年";
    }


    /**
     * 判断两个日期是同年同月
     * @param begin
     * @param end
     * @return
     */
    public static boolean isEqualMonth(Timestamp begin,Timestamp end){
        if (begin.after(end)){
            return false;
        }
        LocalDate lb = toLocalDate(begin);
        LocalDate le = toLocalDate(end);
        return lb.getYear() == le.getYear() && lb.getMonth() == le.getMonth();
    }

    /**
     * 判断两个日期是同年
     * @param begin
     * @param end
     * @return
     */
    public static boolean isEqualYear(Timestamp begin,Timestamp end){
        if (begin.after(end)){
            return false;
        }
        LocalDate lb = toLocalDate(begin);
        LocalDate le = toLocalDate(end);
        return lb.getYear() == le.getYear();
    }

    public static Timestamp endTimeByDateType(DateType type,Timestamp begin,Timestamp end){
        Timestamp result = end;
        switch (type){
            case DAY:
                //如果结束日期与开始日期不再统一个月,或者开始日期小于结束日期，设置结束日期为开始日期的月末
                if (!TimestampHelper.isEqualMonth(begin,end)){
                    result = TimestampHelper.toMonthEnd(begin);
                }
                break;
            case MONTH:
                //如果结束日期与开始日期不再同一年，或者开始日期小于结束日期，设置结束日期为开始日期的年末
                if (!TimestampHelper.isEqualYear(begin,end)){
                    result = TimestampHelper.toYearEnd(begin);
                }
                break;
            default:
                break;
        }
        return result;
    }


    /**
     * 返回两个时间相差的分钟数
     * @param t1
     * @param t2
     * @return
     */
    public static long differByMinute(Timestamp t1,Timestamp t2){
        if (t1.after(t2)){
            long differ = t1.getTime() - t2.getTime();
            return differ/1000/60;
        }
        return 0;
    }

}
