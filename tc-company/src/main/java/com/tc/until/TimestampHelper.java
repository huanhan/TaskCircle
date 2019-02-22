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
     * 某一个小时的开始时间
     * @param timestamp
     * @return
     */
    public static Timestamp toHouseBegin(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        LocalDateTime result = LocalDateTime.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth(),localDateTime.getHour(),0);
        return toTimestamp(result);
    }

    /**
     * 某一个小时的结束时间
     * @param timestamp
     * @return
     */
    public static Timestamp toHouseEnd(Timestamp timestamp){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        LocalDateTime result = LocalDateTime.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth(),localDateTime.getHour(),59,59);
        return toTimestamp(result);
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
     * 将timestamp转成日期
     * 格式 2019年11月11日
     * @param timestamp
     * @return
     */
    public static String toDay(Timestamp timestamp){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.getYear() + "年" + localDateTime.getMonth().getValue() + "月" + localDateTime.getDayOfMonth() + "日";
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
     * 判断两个日期是同年同月同日
     * @param begin
     * @param end
     * @return
     */
    public static boolean isEqualDay(Timestamp begin,Timestamp end){
        if (begin.after(end)){
            return false;
        }
        LocalDate lb = toLocalDate(begin);
        LocalDate le = toLocalDate(end);
        return lb.getYear() == le.getYear() && lb.getMonth() == le.getMonth() && lb.getDayOfMonth() == le.getDayOfMonth();
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
        if (t1.before(t2)){
            long differ = t2.getTime() - t1.getTime();
            return differ/1000/60;
        }
        return 0;
    }

    /**
     * 返回两个时间相差的小时数
     * @param t1
     * @param t2
     * @return
     */
    public static long differByHours(Timestamp t1, Timestamp t2){
        if (t1.before(t2)){
            long differ = t2.getTime() - t1.getTime();
            return differ/1000/60/60;
        }
        return 0;
    }

    /**
     * 返回两个时间相差的天数
     * @param t1
     * @param t2
     * @return
     */
    public static long differByDay(Timestamp t1,Timestamp t2){
        if (t1.before(t2)){
            long differ = t2.getTime() - t1.getTime();
            return differ/1000/60/60/24;
        }
        return 0;
    }

    /**
     * 返回两个时间相差的月数
     * @param t1
     * @param t2
     * @return
     */
    public static long differByMonth(Timestamp t1, Timestamp t2){
        if (t1.before(t2)){
            LocalDateTime lt1 = t1.toLocalDateTime();
            LocalDateTime lt2 = t2.toLocalDateTime();
            int year = lt2.getYear() -  lt1.getYear();
            int month;
            if (year > 1){
                month = (12 - lt1.getMonthValue()) + lt2.getMonthValue();
            }else {
                month = lt2.getMonthValue() - lt1.getMonthValue();
            }
            return (12 * year) + month;
        }
        return 0;
    }

    /**
     * 返回两个时间相差的年数
     * @param t1
     * @param t2
     * @return
     */
    public static long differByYear(Timestamp t1, Timestamp t2){
        if (t1.before(t2)){
            return t2.toLocalDateTime().getYear() - t1.toLocalDateTime().getYear();
        }
        return 0;
    }

    /**
     * 将时间加上一个具体的分钟数
     * @param timestamp
     * @param minute
     * @return
     */
    public static Timestamp addMinute(Timestamp timestamp,int minute){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return TimestampHelper.toTimestamp(localDateTime.plusMinutes(minute));
    }

    /**
     * 将时间加上一个具体的小时数
     * @param timestamp
     * @param hours
     * @return
     */
    public static Timestamp addHours(Timestamp timestamp,int hours){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return TimestampHelper.toTimestamp(localDateTime.plusHours(hours));
    }

    /**
     * 将时间加上一个具体的天数
     * @param timestamp
     * @param day
     * @return
     */
    public static Timestamp addDay(Timestamp timestamp,int day){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return TimestampHelper.toTimestamp(localDateTime.plusDays(day));
    }

    /**
     * 将时间加上一个具体的月
     * @param timestamp
     * @param month
     * @return
     */
    public static Timestamp addMonth(Timestamp timestamp,int month){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return TimestampHelper.toTimestamp(localDateTime.plusMonths(month));
    }

    /**
     * 将时间加上一个具体的年
     * @param timestamp
     * @param year
     * @return
     */
    public static Timestamp addYear(Timestamp timestamp,int year){
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return TimestampHelper.toTimestamp(localDateTime.plusYears(year));
    }

    /**
     * 获取当前时间加一个具体的分钟数
     * @param minute
     * @return
     */
    public static Timestamp addMinuteByToday(int minute){
        return addMinute(TimestampHelper.today(),minute);
    }


}
