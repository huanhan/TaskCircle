package com.tc;

import com.tc.db.entity.User;
import com.tc.until.IdGenerator;
import com.tc.until.TelephoneUtil;
import com.tc.until.TimestampHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextRuntime {
    public static void main(String[] object){
        System.out.println(IdGenerator.INSTANCE.nextId());

        Float f1 = 2f;
        Float f2 = 1.2f;

        BigDecimal b1 = new BigDecimal(f1.toString());
        BigDecimal b2 = new BigDecimal(f2.toString());

        System.out.println(b1.subtract(b2).floatValue());
        System.out.println(b1.add(b2).floatValue());
        System.out.println(b1.multiply(b2).floatValue());
        System.out.println(b1.remainder(b2).floatValue());
        System.out.println(b1.divide(b2,3,0).floatValue());


        Timestamp now = new Timestamp(System.currentTimeMillis());


        LocalDateTime localDateTime = now.toLocalDateTime();
        LocalDate localDate = LocalDate.of(localDateTime.getYear(),localDateTime.getMonth(),localDateTime.getDayOfMonth());
        int year = localDateTime.getYear();
        int monty = localDateTime.getMonth().getValue();
        int day = localDateTime.getDayOfMonth();

        System.out.println(year + "/" + monty + "/" + day);
        Timestamp dayBegin = new Timestamp(Date.from(LocalDate.of(year,11,day).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime());
        System.out.println(dayBegin.getTime());

        if(TimestampHelper.isToday(dayBegin)){
            System.out.println("是当天");
        }else {
            System.out.println("不是当天");
        }

        System.out.println(TimestampHelper.toLocalDate(now).toString());

        System.out.println(TimestampHelper.todayEndByTimestamp());
        System.out.println(localDate.lengthOfMonth());

        System.out.println(TimestampHelper.differByMinute(new Timestamp(System.currentTimeMillis()),
                TimestampHelper.toTimestamp(LocalDateTime.of(2019,1,18,13,50))));

        user();
    }


    public static void user(){
        Field[] fields = User.class.getFields();
        for (Field field:
             fields) {
            try {
                System.out.println(field.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
